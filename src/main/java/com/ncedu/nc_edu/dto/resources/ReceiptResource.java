package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptResource extends RepresentationModel<ReceiptResource> {
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

    private Set<TagResource> tags;

    private List<ReceiptStepResource> steps;

    private UUID owner;
}
