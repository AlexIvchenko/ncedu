package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

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
    private Integer price;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "cooking_method")
    private CookingMethod cookingMethod;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    @OrderColumn(name = "index")
    private List<ReceiptStep> steps;

    @OneToMany(mappedBy = "receipt")
    private Set<IngredientsReceipts> ingredientsReceipts;


    @ManyToMany
    @JoinTable(
            name = "tags_receipts",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
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

    public enum CookingMethod {
        OVEN,
        BLENDER,
        GRILL,
        WOK,
        MICROWAVE,
        FREEZER,
        STEAMER,
        STOVE;
    }

    public enum Cuisine {
        RUSSIAN,
        ITALIAN,
        JAPANESE;
    }

    public Receipt() {}

    public Receipt(Receipt receipt) {
        this.calories = receipt.calories;
        this.carbohydrates = receipt.carbohydrates;
        this.cookingMethod = receipt.cookingMethod;
        this.cuisine = receipt.cuisine;
        this.fats = receipt.fats;
        this.cookingTime = receipt.cookingTime;
        this.id = UUID.randomUUID();
        this.ingredientsReceipts = new HashSet<>(receipt.ingredientsReceipts);
//        this.ingredientsReceiptsDTOs.stream().peek(ingredientsReceipts -> {
//            ingredientsReceipts.setReceipt(this);
//            ingredientsReceipts.getId().setReceiptId(this.id);
//        });
//        this.ingredientsReceipts.forEach(ingredientsReceipts -> {
//            ingredientsReceipts.setReceipt(this);
//            ingredientsReceipts.getId().setReceiptId(this.id);
//        });
        this.name = receipt.name;
        this.owner = null;
        this.price = receipt.price;
        this.proteins = receipt.proteins;
        this.rating = receipt.rating;
        this.steps = new ArrayList<>(receipt.steps);
        //this.steps.stream().peek(receiptStep -> receiptStep.setId(UUID.randomUUID()));
        //this.steps.forEach(receiptStep -> receiptStep.setId(UUID.randomUUID()));
        this.tags = new HashSet<>(receipt.tags);
    }
}