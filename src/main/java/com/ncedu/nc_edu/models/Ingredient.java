package com.ncedu.nc_edu.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "ingredients", schema = "public")
@EqualsAndHashCode(callSuper = false)
public class Ingredient extends RepresentationModel<Ingredient> {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;
}