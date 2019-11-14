package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.assemblers.TagAssembler;
import com.ncedu.nc_edu.dto.resources.TagResource;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        authorities.forEach(log::debug);

        List<TagResource> tags = this.tagService.findAll().stream()
                .map(tag -> {
                    TagResource t = tagAssembler.toModel(tag);
                    t.add(linkTo(methodOn(TagController.class).getById(t.getId())).withSelfRel());
                    if (authorities.contains("ROLE_MODERATOR") || authorities.contains("ROLE_ADMIN")) {
                        t.add(linkTo(methodOn(TagController.class).update(t.getId(), t)).withRel("update"));
                    }
                    return t;
                })
                .collect(Collectors.toList());
        CollectionModel<List<TagResource>> resource = new CollectionModel<>(Collections.singleton(tags));

        if (authorities.contains("ROLE_MODERATOR") || authorities.contains("ROLE_ADMIN")) {
            resource.add(linkTo(methodOn(TagController.class).add(new TagResource())).withRel("create"));
        }

        return resource;
    }

    @GetMapping("/tags/{id}")
    public TagResource getById(@PathVariable UUID id) {
        return tagAssembler.toModel(this.tagService.findById(id));
    }

    @PostMapping("/tags")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public TagResource add(@Valid @RequestBody TagResource newTag) {
        log.debug(newTag.toString());

        return tagAssembler.toModel(tagService.add(newTag));
    }

    @PutMapping("/tags/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public TagResource update(@PathVariable UUID id, @Valid @RequestBody TagResource updatedTag) {
        log.debug(updatedTag.toString());
        updatedTag.setId(id);

        return tagAssembler.toModel(tagService.update(updatedTag));
    }
}
