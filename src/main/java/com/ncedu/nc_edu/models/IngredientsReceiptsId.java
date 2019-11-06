package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Data
public class IngredientsReceiptsId implements Serializable {
    @Column(name = "receipt_id")
    private UUID receiptId;

    @Column(name = "ingredient_id")
    private UUID ingredientId;

    public IngredientsReceiptsId() {
    }

    public IngredientsReceiptsId(UUID receiptId, UUID ingredientId) {
        this.receiptId = receiptId;
        this.ingredientId = ingredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientsReceiptsId that = (IngredientsReceiptsId) o;
        return receiptId.equals(that.receiptId) &&
                ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptId, ingredientId);
    }
}