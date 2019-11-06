package com.ncedu.nc_edu.models;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;

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
    @Type(type = "uuid-char")
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UsersFiltersDTO> usersFiltersDTOs;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserReview> reviews;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @OrderColumn(name = "index")
    private List<ItemCategory> categories;

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }
}
