package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "ingredients_receipts", schema = "public")
@Data
public class IngredientsReceipts {
    @EmbeddedId
    private IngredientsReceiptsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receipt_id")
    private Receipt receipt;

    @Column(name = "value_type")
    private String valueType;

    private Float value;
}