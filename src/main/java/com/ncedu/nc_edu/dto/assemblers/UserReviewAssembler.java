package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.RecipeController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.UserReviewResource;
import com.ncedu.nc_edu.models.UserReview;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class UserReviewAssembler extends RepresentationModelAssemblerSupport<UserReview, UserReviewResource> {
    public UserReviewAssembler() {
        super(UserReview.class, UserReviewResource.class);
    }

    @Override
    public UserReviewResource toModel(UserReview entity) {
        UserReviewResource resource = new UserReviewResource();
        resource.setId(entity.getId());
        resource.setCreated_on(entity.getCreated_on());
        resource.setRating(entity.getRating());
        resource.setReview(entity.getReview());

        resource.add(linkTo(methodOn(RecipeController.class).getById(null, entity.getRecipe().getId())).withRel("receipt").withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getId())).withRel("user").withType("GET"));

        return resource;
    }
}
