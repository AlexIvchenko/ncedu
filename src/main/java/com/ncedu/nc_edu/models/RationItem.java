package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "ration_items", schema = "public")
public class RationItem {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private Date date;

    @ManyToOne
    private ItemCategory category;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Receipt receipt;
}
