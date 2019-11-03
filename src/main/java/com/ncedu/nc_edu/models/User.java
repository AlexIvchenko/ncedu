package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "users", schema = "public")
@Data
public class User {
    public enum Gender {
        MALE, FEMALE, UNKNOWN;
        Gender() {
        }
    }

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type = "uuid-char")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "height")
    private int height;

    @Column(name = "weight")
    private int weight;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles;

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", height=" + height +
                ", weight=" + weight +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}
