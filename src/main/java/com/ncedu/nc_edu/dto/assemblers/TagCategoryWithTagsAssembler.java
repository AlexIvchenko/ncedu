package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.dto.resources.TagCategoryWithTagsResource;
import com.ncedu.nc_edu.models.TagCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TagCategoryWithTagsAssembler
        extends RepresentationModelAssemblerSupport<TagCategory, TagCategoryWithTagsResource> {
    private TagAssembler tagAssembler;

    public TagCategoryWithTagsAssembler(@Autowired TagAssembler tagAssembler) {
        super(TagCategory.class, TagCategoryWithTagsResource.class);
        this.tagAssembler =  tagAssembler;
    }

    @Override
    public TagCategoryWithTagsResource toModel(TagCategory entity) {
        TagCategoryWithTagsResource category = new TagCategoryWithTagsResource();

        category.setId(entity.getId());
        category.setName(entity.getName());
        category.setTags(
                entity.getTags().stream()
                        .map(tag -> tagAssembler.toModel(tag))
                        .collect(Collectors.toSet())
        );

        return category;
    }
}
