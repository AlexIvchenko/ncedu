package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
    private String description;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;
    private Float rating;

    @OneToMany(mappedBy = "receipt")
    private Set<IngredientsReceiptsDTO> ingredientsReceiptsDTOs;

    @ManyToMany
    @JoinTable(
            name = "tags_receipts",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}