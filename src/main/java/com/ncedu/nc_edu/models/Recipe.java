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

    private String state;

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
    @ElementCollection(targetClass = CookingMethod.class)
    @CollectionTable(name = "recipe_cooking_methods", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "method")
    private Set<CookingMethod> cookingMethods;

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