package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.assemblers.TagAssembler;
import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class TagController {
    private TagService tagService;
    private TagAssembler tagAssembler;

    public TagController(@Autowired TagService tagService, @Autowired TagAssembler tagAssembler) {
        this.tagAssembler = tagAssembler;
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public CollectionModel<List<TagResource>> getAll(Authentication auth) {

        List<TagResource> tags = this.tagService.findAll().stream()
                .map(tag -> {
                    TagResource t = tagAssembler.toModel(tag);

                    // todo link to search by this tag
                    //.add(linkTo(methodOn(TagController.class).getById(t.getId())).withSelfRel());

                    return t;
                })
                .collect(Collectors.toList());

        CollectionModel<List<TagResource>> resource = new CollectionModel<>(Collections.singleton(tags));

        return resource;
    }

    @GetMapping("/tags/{id}/receipts")
    public PagedModel<Receipt> getAllByTag(@PathVariable UUID id, Authentication auth) {
        return null;
    }
}
