package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;
import java.util.UUID;


/**
 * Receipt resource without steps.
 * We don't want to retrieve full receipt information for lists etc.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptLightweightResource extends RepresentationModel<ReceiptLightweightResource> {
    private UUID id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @PositiveOrZero(message = "calories must be positive")
    private Integer calories;

    @PositiveOrZero(message = "proteins must be positive")
    private Float proteins;

    @PositiveOrZero(message = "fats must be positive")
    private Float fats;

    @PositiveOrZero(message = "carbohydrates must be positive")
    private Float carbohydrates;

    private Float rating;

    private UUID owner;

    private Set<TagResource> tags;
}
