package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.RecipeController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.RecipeResource;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.security.SecurityUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeResource> {
    private final SecurityUtils securityUtils;
    private final IngredientAssembler ingredientAssembler;

    public RecipeAssembler(@Autowired SecurityUtils securityUtils,
                            @Autowired IngredientAssembler ingredientAssembler) {
        super(Recipe.class, RecipeResource.class);
        this.securityUtils = securityUtils;
        this.ingredientAssembler = ingredientAssembler;
    }

    @Override
    public RecipeResource toModel(Recipe entity) {
        Authentication auth = securityUtils.getAuthentication();
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
        resource.setCookingMethod(entity.getCookingMethod().toString());
        resource.setCuisine(entity.getCuisine().toString());

        resource.setTags(entity.getTags().stream()
                .map(Tag::getName).collect(Collectors.toSet())
        );

        resource.setIngredients(entity.getIngredientsRecipes().stream()
                .map(ingredientRecipe -> {
                    JSONObject json = new JSONObject();
                    json.put("id", ingredientRecipe.getIngredient().getId());
                    json.put("name", ingredientRecipe.getIngredient().getName());
                    json.put("valueType", ingredientRecipe.getValueType());
                    json.put("value", ingredientRecipe.getValue());
                    return json;
                }).collect(Collectors.toList())
        );

        resource.add(linkTo(methodOn(RecipeController.class).getById(auth, entity.getId())).withSelfRel().withType("GET"));
        resource.add(linkTo(methodOn(RecipeController.class).getRecipeSteps(entity.getId())).withRel("steps").withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getOwner().getId())).withRel("owner").withType("GET"));

        if (securityUtils.isRecipesOwnerOrGranted(entity.getId())) {
            resource.add(linkTo(methodOn(RecipeController.class).update(auth, entity.getId(), null)).withRel("update").withType("PUT"));
            resource.add(linkTo(methodOn(RecipeController.class).remove(auth, entity.getId())).withRel("remove").withType("DELETE"));
        }

        resource.add(linkTo(methodOn(RecipeController.class).create(auth, null)).withRel("create"));

        return resource;
    }
}
