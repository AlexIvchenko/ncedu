package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.models.Tag;

import java.util.List;
import java.util.UUID;

public interface TagService {

    List<Tag> findAll();

    Tag findById(UUID id);

    Tag add(TagResource newTag);

    Tag update(TagResource updatedTag);
}
