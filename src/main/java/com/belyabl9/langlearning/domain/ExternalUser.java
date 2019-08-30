package com.belyabl9.langlearning.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class ExternalUser extends User implements Serializable {
    
    private String externalId;
    
    private SocialProvider socialProvider;

    public ExternalUser() {
        super();
    }
    
    public ExternalUser(String externalId, SocialProvider socialProvider, String name, String email) {
        super(name, email, null, "", true);
        this.externalId = externalId;
        this.socialProvider = socialProvider;
    }

    public boolean isAdmin() {
        return false;
    }
}
