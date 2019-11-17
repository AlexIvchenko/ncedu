package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.ReceiptController;
import com.ncedu.nc_edu.controllers.TagController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReceiptAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptResource> {
    private final TagAssembler tagAssembler;
    private final StepAssembler stepAssembler;

    public ReceiptAssembler(@Autowired TagAssembler tagAssembler, @Autowired StepAssembler stepAssembler) {
        super(Receipt.class, ReceiptResource.class);
        this.tagAssembler = tagAssembler;
        this.stepAssembler = stepAssembler;
    }

    @Override
    public ReceiptResource toModel(Receipt entity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<GrantedAuthority> roles = new HashSet<>(
                (SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ReceiptResource resource = new ReceiptResource();

        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCalories(entity.getCalories());
        resource.setFats(entity.getFats());
        resource.setCarbohydrates(entity.getCarbohydrates());
        resource.setProteins(entity.getProteins());
        resource.setRating(entity.getRating());
        //resource.setOwner(entity.getOwner().getId());

        //resource.setTags(entity.getTags().stream().map(tagAssembler::toModel).collect(Collectors.toSet()));

        //resource.setSteps(entity.getSteps().stream().map(stepAssembler::toModel).collect(Collectors.toList()));

        resource.add(linkTo(methodOn(ReceiptController.class).getById(auth, entity.getId())).withSelfRel().withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getOwner().getId())).withRel("owner").withType("GET"));

        if (roles.contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))
                || roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || currentUser.getUser().getId().equals(entity.getOwner().getId())
        ) {
            resource.add(linkTo(methodOn(ReceiptController.class).update(auth, entity.getId(), null)).withRel("update").withType("PUT"));
            resource.add(linkTo(methodOn(ReceiptController.class).remove(auth, entity.getId())).withRel("remove").withType("DELETE"));
        }

        resource.add(linkTo(methodOn(ReceiptController.class).create(auth, null)).withRel("create"));

        return resource;
    }
}
