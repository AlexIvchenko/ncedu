package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.models.TagCategory;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TagCategoryAssembler extends RepresentationModelAssemblerSupport<TagCategory, TagCategoryResource> {
    public TagCategoryAssembler() {
        super(TagCategory.class, TagCategoryResource.class);
    }

    @Override
    public TagCategoryResource toModel(TagCategory entity) {
        TagCategoryResource category = new TagCategoryResource();

        category.setId(entity.getId());
        category.setName(entity.getName());

        return category;
    }
}
