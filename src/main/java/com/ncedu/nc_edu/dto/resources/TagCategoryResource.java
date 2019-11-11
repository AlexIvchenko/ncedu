package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class TagCategoryResource extends RepresentationModel<TagCategoryResource> {
    private UUID id;

    @NotBlank
    private String name;
}
