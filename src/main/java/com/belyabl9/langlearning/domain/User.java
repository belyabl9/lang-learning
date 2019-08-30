package com.belyabl9.langlearning.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;
    
    protected String name;

    @Column(unique = true)
    @Email
    protected String email;

    @Column(unique = true)
    protected String login;

    private String password;
    protected boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Category> categories = new ArrayList<>();

    @Column
    protected Language learningLang;

    protected User() {}
    
    protected User(String name, String email, String login, String password, boolean enabled) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.enabled = enabled;
    }
    
    public boolean isAdmin() {
        for (UserRole userRole : userRoles) {
            if (userRole.getName().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
