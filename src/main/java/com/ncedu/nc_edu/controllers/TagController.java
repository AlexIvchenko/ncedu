package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.assemblers.TagAssembler;
import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    public CollectionModel<TagResource> getAll(
            Authentication auth,
            @RequestParam(value = "name", required = false) String name
    ) {
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        List<Tag> tagEntities;

        if (name == null) {
            tagEntities = this.tagService.findAll();
        } else {
            tagEntities = this.tagService.findAllByNameContains(name);
        }

        if (tagEntities.size() == 0) {
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(TagResource.class);
            List<EmbeddedWrapper> list = Collections.singletonList(wrapper);
            return new CollectionModel(list);
        }

        List<TagResource> tags = tagEntities.stream()
                .map(tag -> {
                    TagResource t = tagAssembler.toModel(tag);

                    // todo link to search by this tag
                    //.add(linkTo(methodOn(TagController.class).getById(t.getId())).withSelfRel());

                    return t;
                })
                .collect(Collectors.toList());

        CollectionModel<TagResource> resource = new CollectionModel<>(tags);

        return resource;
    }

    @GetMapping("/tags/{id}/receipts")
    public PagedModel<Receipt> getAllByTag(@PathVariable UUID id, Authentication auth) {
        return null;
    }
}
