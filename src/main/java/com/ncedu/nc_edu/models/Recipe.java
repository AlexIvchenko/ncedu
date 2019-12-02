package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "recipes", schema = "public")
@Data
public class Recipe {
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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "index")
    private List<RecipeStep> steps;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IngredientsRecipes> ingredientsRecipes;

    @ManyToMany
    @JoinTable(
            name = "tags_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    private Set<Tag> tags;

    @ManyToOne
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
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

    public Recipe() {}

    public Recipe(Recipe recipe) {
        this.calories = recipe.calories;
        this.carbohydrates = recipe.carbohydrates;
        this.cookingMethod = recipe.cookingMethod;
        this.cuisine = recipe.cuisine;
        this.fats = recipe.fats;
        this.cookingTime = recipe.cookingTime;
        this.id = UUID.randomUUID();
        this.ingredientsRecipes = new HashSet<>(recipe.ingredientsRecipes);
//        this.ingredientsReceiptsDTOs.stream().peek(ingredientsReceipts -> {
//            ingredientsReceipts.setReceipt(this);
//            ingredientsReceipts.getId().setReceiptId(this.id);
//        });
//        this.ingredientsReceipts.forEach(ingredientsReceipts -> {
//            ingredientsReceipts.setReceipt(this);
//            ingredientsReceipts.getId().setReceiptId(this.id);
//        });
        this.name = recipe.name;
        this.owner = null;
        this.price = recipe.price;
        this.proteins = recipe.proteins;
        this.rating = recipe.rating;
        this.steps = new ArrayList<>(recipe.steps);
        //this.steps.stream().peek(receiptStep -> receiptStep.setId(UUID.randomUUID()));
        //this.steps.forEach(receiptStep -> receiptStep.setId(UUID.randomUUID()));
        this.tags = new HashSet<>(recipe.tags);
    }
    
    public void setSteps(List<RecipeStep> steps) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }

        this.steps.clear();
        if (steps != null) {
            this.steps.addAll(steps);
        }
    }
}