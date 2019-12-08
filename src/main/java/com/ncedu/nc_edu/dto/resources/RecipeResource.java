package com.ncedu.nc_edu.dto.resources;

import com.ncedu.nc_edu.models.Recipe;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class RecipeResource extends RepresentationModel<RecipeResource> {
    private UUID id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255)
    private String name;

    @PositiveOrZero(message = "Calories must be positive")
    @Size(max = 1000)
    private Integer calories;

    @PositiveOrZero(message = "Proteins must be positive")
    @Size(max = 100)
    private Float proteins;

    @PositiveOrZero(message = "Fats must be positive")
    @Size(max = 100)
    private Float fats;

    @PositiveOrZero(message = "Carbohydrates must be positive")
    @Size(max = 100)
    private Float carbohydrates;

    private Float rating;

    @Positive(message = "Cooking time must be positive")
    @Size(max = 1440)
    private Integer cookingTime;

    @Positive(message = "Price time must be positive")
    @Size(max = 10000)
    private Integer price;

    @NotNull
    private Set<Recipe.CookingMethod> cookingMethods;

    @NotNull
    private Recipe.Cuisine cuisine;

    private Set<String> tags;

    private List<RecipeIngredientResource> ingredients;

    /**
     * Field only for returning. Should be never updated.
     */
    private UUID owner;
}
