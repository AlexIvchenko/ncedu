package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.exceptions.AlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.repositories.TagRepository;
import com.ncedu.nc_edu.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    public TagServiceImpl(@Autowired TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(UUID id) {
        return tagRepository.findById(id).orElseThrow(()-> new EntityDoesNotExistsException("tag"));
    }

    @Override
    public Tag add(TagResource newTag) {
        tagRepository.findByName(newTag.getName()).orElseThrow(() -> new AlreadyExistsException("Tag", "name"));

        Tag tag = new Tag();
        tag.setId(UUID.randomUUID());
        tag.setName(newTag.getName());

        return tagRepository.save(tag);
    }

    @Override
    public Tag update(TagResource updatedTag) {
        Tag oldTag = tagRepository.findById(updatedTag.getId()).orElseThrow(() -> new EntityDoesNotExistsException("Tag"));
        tagRepository.findByName(updatedTag.getName()).orElseThrow(() -> new AlreadyExistsException("Tag", "name"));

        oldTag.setName(updatedTag.getName());

        return tagRepository.save(oldTag);
    }
}
