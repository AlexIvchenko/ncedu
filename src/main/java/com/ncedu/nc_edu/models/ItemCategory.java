package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ration_categories")
@Data
public class ItemCategory {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;
}
