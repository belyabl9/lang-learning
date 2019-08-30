package com.belyabl9.langlearning.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class UserRole extends BaseEntity implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private String name;

    public UserRole() {
    }

    public UserRole(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
