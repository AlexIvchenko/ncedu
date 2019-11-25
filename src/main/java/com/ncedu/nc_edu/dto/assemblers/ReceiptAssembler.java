package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.ReceiptController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.security.CustomUserDetails;
import com.ncedu.nc_edu.security.SecurityUtils;
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

    private final SecurityUtils securityUtils;

    public ReceiptAssembler(@Autowired SecurityUtils securityUtils) {
        super(Receipt.class, ReceiptResource.class);
        this.securityUtils = securityUtils;
    }

    @Override
    public ReceiptResource toModel(Receipt entity) {
        Authentication auth = securityUtils.getAuthentication();
        ReceiptResource resource = new ReceiptResource();

        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCalories(entity.getCalories());
        resource.setFats(entity.getFats());
        resource.setCarbohydrates(entity.getCarbohydrates());
        resource.setProteins(entity.getProteins());
        resource.setRating(entity.getRating());
        resource.setCookingTime(entity.getCookingTime());
        resource.setPrice(entity.getPrice());
        resource.setCookingMethod(entity.getCookingMethod().toString());
        resource.setCuisine(entity.getCuisine().toString());

        resource.setTags(entity.getTags().stream()
                .map(Tag::getName).collect(Collectors.toSet())
        );

        resource.add(linkTo(methodOn(ReceiptController.class).getById(auth, entity.getId())).withSelfRel().withType("GET"));
        resource.add(linkTo(methodOn(ReceiptController.class).getReceiptSteps(entity.getId())).withRel("steps").withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getOwner().getId())).withRel("owner").withType("GET"));

        if (securityUtils.isReceiptsOwnerOrGranted(entity.getId())) {
            resource.add(linkTo(methodOn(ReceiptController.class).update(auth, entity.getId(), null)).withRel("update").withType("PUT"));
            resource.add(linkTo(methodOn(ReceiptController.class).remove(auth, entity.getId())).withRel("remove").withType("DELETE"));
        }

        resource.add(linkTo(methodOn(ReceiptController.class).create(auth, null)).withRel("create"));

        return resource;
    }
}
