package com.belyabl9.langlearning.domain;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class InternalUser extends User implements Serializable {

    public InternalUser() {
        super();
    }

    public InternalUser(String name, String email, String login, String password, boolean enabled) {
        super(name, email, login, password, enabled);
    }
}
