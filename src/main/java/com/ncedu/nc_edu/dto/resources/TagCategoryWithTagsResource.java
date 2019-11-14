package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class TagCategoryWithTagsResource extends RepresentationModel<TagCategoryWithTagsResource> {
    private UUID id;

    @NotBlank
    private String name;

    private Set<TagResource> tags;
}
