package com.ncedu.nc_edu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "roles", schema = "public")
@Data
public class UserRole {
    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type = "uuid-char")
    private UUID id;

    @Column(name = "role")
    private String role;
}
