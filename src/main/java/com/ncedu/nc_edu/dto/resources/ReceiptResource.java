package com.ncedu.nc_edu.dto.resources;

import com.ncedu.nc_edu.dto.validators.ValueOfEnum;
import com.ncedu.nc_edu.models.Receipt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptResource extends RepresentationModel<ReceiptResource> {
    private UUID id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255)
    private String name;

    @PositiveOrZero(message = "Calories must be positive")
    @Size(max = 1000)
    private Integer calories;

    @PositiveOrZero(message = "Proteins must be positive")
    @Size(max = 1000)
    private Float proteins;

    @PositiveOrZero(message = "Fats must be positive")
    @Size(max = 1000)
    private Float fats;

    @PositiveOrZero(message = "Carbohydrates must be positive")
    @Size(max = 1000)
    private Float carbohydrates;

    private Float rating;

    @Positive(message = "Cooking time must be positive")
    @Size(max = 1440)
    private Integer cookingTime;

    @Positive(message = "Price time must be positive")
    @Size(max = 10000)
    private Integer price;

    @NotNull
    @ValueOfEnum(value = Receipt.CookingMethod.class, message = "Cooking method must be any of " +
            "OVEN|BLENDER|GRILL|WOK|MICROWAVE|FREEZER|STEAMER|STOVE")
    private String cookingMethod;

    @NotNull
    @ValueOfEnum(value = Receipt.Cuisine.class, message = "Cuisine must be any of " +
            "RUSSIAN|ITALIAN|JAPANESE")
    private String cuisine;

    private Set<String> tags;

    //private List<ReceiptStepResource> steps;

    /**
     * Field only for returning. Should be never updated.
     */
    //private UUID owner;
}
