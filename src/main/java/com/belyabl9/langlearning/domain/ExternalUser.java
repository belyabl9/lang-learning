package com.belyabl9.langlearning.domain;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public SocialProvider getSocialProvider() {
        return socialProvider;
    }

    public void setSocialProvider(SocialProvider socialProvider) {
        this.socialProvider = socialProvider;
    }
}
