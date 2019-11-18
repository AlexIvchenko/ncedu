package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.models.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();

    Tag findByName(String name);
    boolean existsByName(String name);
    Tag add(TagResource newTag);
}