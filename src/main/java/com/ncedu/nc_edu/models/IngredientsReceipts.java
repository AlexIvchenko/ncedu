package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ingredients_receipts", schema = "public")
@Data
public class IngredientsReceipts {
    @EmbeddedId
    private IngredientsReceiptsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiptId")
    private Receipt receipt;

    @Column(name = "value_type")
    private String valueType;

    private Float value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientsReceipts that = (IngredientsReceipts) o;
        return ingredient.equals(that.ingredient) &&
                receipt.equals(that.receipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, receipt);
    }
}