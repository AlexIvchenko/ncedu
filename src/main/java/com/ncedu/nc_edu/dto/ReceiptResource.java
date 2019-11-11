package com.ncedu.nc_edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptResource extends RepresentationModel<ReceiptResource> {
    private UUID id;

    @NotNull(message = "Cannot be empty")
    private String name;

    @PositiveOrZero(message = "Must be positive or 0")
    private Integer calories;

    @PositiveOrZero(message = "Must be positive or 0")
    private Float proteins;

    @PositiveOrZero(message = "Must be positive or 0")
    private Float fats;

    @PositiveOrZero(message = "Must be positive or 0")
    private Float carbohydrates;

    @Min(value = 0, message = "Cannot be less than 0")
    @Max(value = 5, message = "Cannot be more than 5")
    private Float rating;
}
