package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.assemblers.RecipeAssembler;
import com.ncedu.nc_edu.dto.assemblers.RecipeStepAssembler;
import com.ncedu.nc_edu.dto.assemblers.UserReviewAssembler;
import com.ncedu.nc_edu.dto.resources.*;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.security.CustomUserDetails;
import com.ncedu.nc_edu.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeAssembler recipeAssembler;
    private final UserReviewAssembler userReviewAssembler;
    private final RecipeStepAssembler recipeStepAssembler;

    public RecipeController(
            @Autowired RecipeService recipeService,
            @Autowired RecipeAssembler recipeAssembler,
            @Autowired UserReviewAssembler userReviewAssembler,
            @Autowired RecipeStepAssembler recipeStepAssembler
    ) {
        this.recipeService = recipeService;
        this.recipeAssembler = recipeAssembler;
        this.userReviewAssembler = userReviewAssembler;
        this.recipeStepAssembler = recipeStepAssembler;
    }

    @GetMapping("/recipes")
    public PagedModel getAll(
            Authentication auth,
            Pageable pageable
    ) {
        Page<Recipe> page = this.recipeService.findAll(pageable);

        PagedModel paged = PagedModel.wrap(page.getContent().stream().map(recipeAssembler::toModel)
                .collect(Collectors.toList()), new PagedModel.PageMetadata(
                page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()
        ));

        paged.add(linkTo(methodOn(RecipeController.class).create(auth, null)).withRel("create"));

        return paged;
    }

    @GetMapping(value = "/recipes/{id}")
    public RecipeResource getById(Authentication auth, @PathVariable UUID id) {
        RecipeResource resource = this.recipeAssembler.toModel(this.recipeService.findById(id));
        return resource;
    }

    @GetMapping(value = "/recipes/{id}/reviews")
    public ResponseEntity<CollectionModel<UserReviewResource>> getReviews(@PathVariable UUID id) {
        CollectionModel resource = userReviewAssembler.toCollectionModel(recipeService.findReviewsById(id));
        return ResponseEntity.ok(resource);
    }

    @PostMapping(value = "/recipes/{id}/reviews")
    public ResponseEntity<RepresentationModel<UserReviewResource>> addReview(@PathVariable UUID id,
                                                                  @RequestBody UserReviewResource reviewResource) {
        UserReviewResource resource = userReviewAssembler.toModel(recipeService.addReview(id, reviewResource));
        return ResponseEntity.ok(resource);
    }

    @GetMapping(value = "/recipes/{id}/reviews/{reviewId}")
    public ResponseEntity<RepresentationModel<UserReviewResource>> getReviews(@PathVariable UUID id,
                                                                          @PathVariable UUID reviewId) {
        UserReviewResource resource = userReviewAssembler.toModel(recipeService.findReviewByIds(id, reviewId));
        return ResponseEntity.ok(resource);
    }

    @GetMapping(value = "/recipes/{id}/steps")
    public CollectionModel<RecipeStepResource> getRecipeSteps(@PathVariable UUID id) {
        CollectionModel<RecipeStepResource> resource = new CollectionModel<>(
                this.recipeService.findById(id).getSteps().stream()
                        .map(recipeStepAssembler::toModel).collect(Collectors.toList())
        );

        if (resource.getContent().size() == 0) {
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(RecipeStepResource.class);
            List<EmbeddedWrapper> list = Collections.singletonList(wrapper);
            return new CollectionModel(list);
        }

        resource.add(linkTo(methodOn(RecipeController.class).getById(null, id)).withRel("recipe"));

        return resource;
    }

    @PostMapping(value = "/recipes")
    public RecipeResource create(Authentication auth, @RequestBody @Valid RecipeWithStepsResource recipe) {
        User user = ((CustomUserDetails)(auth.getPrincipal())).getUser();

        if (recipe.getSteps() == null) {
            throw new RequestParseException("Recipe must contain at least 1 step");
        }

        recipe.getSteps().forEach(step -> {
            if (step.getDescription() == null && step.getPicture() == null) {
                throw new RequestParseException("Step must contain either picture or description");
            }
        });

        if (recipe.getInfo().getCookingMethods() == null) {
            throw new RequestParseException("Recipe must contain at least 1 cooking method");
        } else {
            if (recipe.getInfo().getCookingMethods().size() == 0) {
                throw new RequestParseException("Recipe must contain at least 1 cooking method");
            }
        }

        log.debug(recipe.getInfo().toString());

        RecipeResource resource = this.recipeAssembler.toModel(this.recipeService.create(recipe, user));

        log.debug(resource.toString());

        return resource;
    }

    @PutMapping(value = "/recipes/{id}")
    public RecipeResource update(
            Authentication auth,
            @PathVariable UUID id,
            @RequestBody @Valid RecipeWithStepsResource recipe
    ) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();

        if (recipe.getSteps() == null) {
            throw new RequestParseException("Recipe must contain at least 1 step");
        }

        recipe.getSteps().forEach(step -> {
            if (step.getDescription() == null && step.getPicture() == null) {
                throw new RequestParseException("Step must contain either picture or description");
            }
        });

        recipe.getInfo().setId(id);

        RecipeResource updatedResource = this.recipeAssembler.toModel(this.recipeService.update(recipe));

        return updatedResource;
    }

    @DeleteMapping(value = "/recipes/{id}")
    public ResponseEntity<Void> remove(Authentication auth, @PathVariable UUID id) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();

        this.recipeService.removeById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/recipes/search")
    public PagedModel search(
            Authentication auth,
            @Valid RecipeSearchCriteria recipeSearchCriteria,
            Pageable pageable
    ) {
        if (recipeSearchCriteria == null) {
            return this.getAll(auth, pageable);
        }

        if (!recipeSearchCriteria.hasAnyCriteria()) {
            return this.getAll(auth, pageable);
        }

        Page<Recipe> page = recipeService.search(recipeSearchCriteria, pageable);

        PagedModel paged = PagedModel.wrap(page.getContent().stream().map(recipeAssembler::toModel)
                        .collect(Collectors.toList()), new PagedModel.PageMetadata(
                                page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages())
        );

//        resource.add(linkTo(methodOn(RecipeController.class).search(auth, recipeSearchCriteria, pageable.next())).withRel("next"));
//        resource.add(linkTo(methodOn(RecipeController.class).search(auth, recipeSearchCriteria, pageable.previousOrFirst())).withRel("prev"));
//        resource.add(linkTo(methodOn(RecipeController.class).search(auth, recipeSearchCriteria, pageable.first())).withRel("first"));

        return paged;
    }

    @PostMapping(value = "/recipes/{id}/clone")
    public RecipeResource cloneRecipe(
            Authentication auth,
            @PathVariable UUID id
    ) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        var tmp = this.recipeService.cloneRecipe(id, user);
        return this.recipeAssembler.toModel(tmp);
    }

    @GetMapping("/recipes/cookingMethods")
    public CollectionModel<String> getAvailableCookingMethods() {
        return new CollectionModel<>(
                Stream.of(Recipe.CookingMethod.values()).map(Enum::toString).collect(Collectors.toSet())
        );
    }

    @GetMapping("/recipes/cuisines")
    public CollectionModel<String> getAvailableCuisines() {
        return new CollectionModel<>(
                Stream.of(Recipe.Cuisine.values()).map(Enum::toString).collect(Collectors.toSet())
        );
    }
}
