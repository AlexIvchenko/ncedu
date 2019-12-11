package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.ModeratorController;
import com.ncedu.nc_edu.controllers.RecipeController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.RecipeResource;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.security.SecurityAccessResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeResource> {
    private final SecurityAccessResolver securityAccessResolver;
    private final RecipeIngredientAssembler recipeIngredientAssembler;

    public RecipeAssembler(@Autowired SecurityAccessResolver securityAccessResolver,
                           @Autowired RecipeIngredientAssembler recipeIngredientAssembler) {
        super(Recipe.class, RecipeResource.class);
        this.securityAccessResolver = securityAccessResolver;
        this.recipeIngredientAssembler = recipeIngredientAssembler;
    }

    @Override
    public RecipeResource toModel(Recipe entity) {
        Authentication auth = securityAccessResolver.getAuthentication();
        RecipeResource resource = new RecipeResource();

        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCalories(entity.getCalories());
        resource.setFats(entity.getFats());
        resource.setCarbohydrates(entity.getCarbohydrates());
        resource.setProteins(entity.getProteins());
        resource.setRating(entity.getRating());
        resource.setCookingTime(entity.getCookingTime());
        resource.setPrice(entity.getPrice());
        resource.setCookingMethods(entity.getCookingMethods());
        resource.setCuisine(entity.getCuisine());
        resource.setOwner(entity.getOwner().getId());
        resource.setReviewsNumber(entity.getReviewsNumber());

        resource.setTags(entity.getTags().stream()
                .map(Tag::getName).collect(Collectors.toSet())
        );

        resource.setIngredients(entity.getIngredientsRecipes().stream()
                .map(this.recipeIngredientAssembler::toModel).collect(Collectors.toList())
        );

        resource.add(linkTo(methodOn(RecipeController.class).getById(auth, entity.getId())).withSelfRel().withType("GET"));
        resource.add(linkTo(methodOn(RecipeController.class).getRecipeSteps(entity.getId())).withRel("steps").withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getOwner().getId())).withRel("owner").withType("GET"));
        resource.add(linkTo(methodOn(RecipeController.class).getReviews(entity.getId())).withRel("reviews").withType("GET"));


        if (securityAccessResolver.isRecipeOwnerOrGranted(entity.getId())) {
            boolean editedFlag = false;
            if (entity.getOriginalRef() != null) {
                resource.setIsEditedClone(true);
                resource.setId(entity.getOriginalRef().getId());
                editedFlag = true;
            }

            resource.setState(entity.getState().toString());
            resource.setModeratorComment(entity.getModeratorComment());

            if (securityAccessResolver.isModerator()) {
                if (entity.getState().equals(Recipe.State.WAITING_FOR_APPROVAL) ||
                        entity.getState().equals(Recipe.State.EDITED)
                ) {
                    UUID id = editedFlag ? entity.getOriginalRef().getId() : entity.getId();

                    resource.add(linkTo(methodOn(ModeratorController.class).approveRecipeOrChanges(id)).withRel("approve").withType("POST"));
                    resource.add(linkTo(methodOn(ModeratorController.class).declineChangesOrApproval(id)).withRel("decline").withType("POST"));

                    if (entity.getState().equals(Recipe.State.EDITED)) {
                        resource.add(linkTo(methodOn(ModeratorController.class).cloneRecipeChanges(id)).withRel("cloneChanges").withType("POST"));
                    }
                }
            }

            if (entity.getState().equals(Recipe.State.EDITABLE)) {
                resource.add(linkTo(methodOn(RecipeController.class).requestForApproval(entity.getId())).withRel("approve").withType("PUT"));
            }

            resource.add(linkTo(methodOn(RecipeController.class).update(auth, entity.getId(), null)).withRel("update").withType("PUT"));
            resource.add(linkTo(methodOn(RecipeController.class).remove(auth, entity.getId())).withRel("remove").withType("DELETE"));
        }

        resource.add(linkTo(methodOn(RecipeController.class).create(auth, null)).withRel("create"));

        return resource;
    }
}
