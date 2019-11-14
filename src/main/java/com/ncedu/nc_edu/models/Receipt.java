package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "receipts", schema = "public")
@Data
public class Receipt {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String name;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;
    private Float rating;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    @OrderColumn(name = "index")
    private List<ReceiptStep> steps;

    @OneToMany(mappedBy = "receipt")
    private Set<IngredientsReceiptsDTO> ingredientsReceiptsDTOs;

    @ManyToMany
    @JoinTable(
            name = "tags_receipts",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return id.equals(receipt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", carbohydrates=" + carbohydrates +
                ", rating=" + rating +
                '}';
    }
}