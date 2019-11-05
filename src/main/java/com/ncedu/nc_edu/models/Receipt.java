package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "receipts", schema = "public")
@Data
public class Receipt {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String name;
    private String description;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;
    private Float rating;
}