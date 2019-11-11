package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "receipt_steps", schema = "public")
public class ReceiptStep {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String description;

    @ManyToOne
    private Receipt receipt;

    @Type(type = "uuid-char")
    private UUID picture;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptStep that = (ReceiptStep) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReceiptStep{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", picture=" + picture +
                '}';
    }
}
