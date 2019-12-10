package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.*;
import com.ncedu.nc_edu.exceptions.AlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.*;
import com.ncedu.nc_edu.repositories.RecipeRepository;
import com.ncedu.nc_edu.security.SecurityAccessResolver;
import com.ncedu.nc_edu.services.IngredientService;
import com.ncedu.nc_edu.services.RecipeService;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ncedu.nc_edu.models.Recipe.State.*;

;

@Service
@Transactional
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final TagService tagService;
    private final IngredientService ingredientService;
    private final SecurityAccessResolver securityAccessResolver;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            TagService tagService,
            IngredientService ingredientService,
            SecurityAccessResolver securityAccessResolver) {
        this.recipeRepository = recipeRepository;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
        this.securityAccessResolver = securityAccessResolver;
    }

    public Page<Recipe> findAll(Pageable pageable) {
        return this.recipeRepository.findAllByVisibleIsTrue(pageable);
    }

    public Recipe findById(UUID id) {
        Recipe recipe = this.recipeRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        if (recipe.getOwner().equals(securityAccessResolver.getUser()) || securityAccessResolver.isModerator()) {
            return recipe;
        }

        if (recipe.isVisible()) {
            return recipe;
        }

        throw new EntityDoesNotExistsException("Recipe");
    }

    public List<Recipe> findByName(String name) {
        List<Recipe> recipes = this.recipeRepository.findByNameContaining(name);

        recipes = recipes.stream().filter(recipe ->
                recipe.getOwner().equals(securityAccessResolver.getUser()) || securityAccessResolver.isModerator()
        ).collect(Collectors.toList());

        return recipes;
    }

    @Override
    public List<Recipe> findAllOwn(User user) {
        return this.recipeRepository.findByOwner(user);
    }

    @Override
    public boolean removeById(UUID id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (recipe.getState()) {
            case CHANGES_NEEDED:
            case WAITING_FOR_APPROVAL:
                recipe.setVisible(false);
                recipe.setState(Recipe.State.DELETED);
                this.recipeRepository.save(recipe);
                return true;

            case DELETED:
                throw new EntityDoesNotExistsException("Recipe");

            case EDITED:
                if (securityAccessResolver.isModerator()) {
                    return false;
                } else {
                    throw new EntityDoesNotExistsException("Recipe");
                }

            case PUBLISHED:
                if (securityAccessResolver.isModerator()) {
                    recipe.setVisible(false);
                    recipe.setState(Recipe.State.DELETED);
                    this.recipeRepository.save(recipe);
                    return true;
                } else {
                    throw new EntityDoesNotExistsException("Recipe");
                }

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    @Override
    public boolean moderatorApprove(UUID id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (recipe.getState()) {
            case WAITING_FOR_APPROVAL:
                this.approveWaitingForApprovalRecipe(recipe);
                return true;

            case EDITED:
                this.approveEditedRecipe(recipe);
                return true;

            case CHANGES_NEEDED:
            case DELETED:
            case PUBLISHED:
                return false;

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    // Merge cloned with changes into original and delete cloned
    private void approveEditedRecipe(Recipe recipe) {
        Recipe editedRecipe = recipe.getClonedRef();

        recipe.setVisible(true);
        recipe.setClonedRef(null);
        recipe.setState(PUBLISHED);
        recipe.setCalories(editedRecipe.getCalories());
        recipe.setCarbohydrates(editedRecipe.getCarbohydrates());
        recipe.setCookingTime(editedRecipe.getCookingTime());
        recipe.setCuisine(editedRecipe.getCuisine());
        recipe.setFats(editedRecipe.getFats());
        recipe.setProteins(editedRecipe.getProteins());
        recipe.setPrice(editedRecipe.getPrice());
        recipe.setName(editedRecipe.getName());

        recipe.setTags(new HashSet<>(editedRecipe.getTags()));
        recipe.setCookingMethods(new HashSet<>(editedRecipe.getCookingMethods()));

        List<RecipeStep> steps = new ArrayList<>();
        recipe.removeSteps();

        editedRecipe.getSteps().forEach(recipeStep -> {
            RecipeStep tempStep = new RecipeStep();
            tempStep.setId(UUID.randomUUID());
            tempStep.setRecipe(recipe);
            tempStep.setDescription(recipeStep.getDescription());
            tempStep.setPicture(recipeStep.getPicture());
            steps.add(tempStep);
        });

        recipe.setSteps(steps);

        Set<IngredientsRecipes> ingredientsRecipes = new HashSet<>();

        editedRecipe.getIngredientsRecipes().forEach(ingredientsRecipes1 -> {
            Ingredient ingredient = ingredientService.findById(ingredientsRecipes1.getIngredient().getId());
            IngredientsRecipes temp = new IngredientsRecipes();

            temp.setIngredient(ingredient);
            temp.setRecipe(recipe);
            temp.setValue(ingredientsRecipes1.getValue());
            temp.setValueType(ingredientsRecipes1.getValueType());

            ingredientsRecipes.add(temp);
        });

        recipe.getIngredientsRecipes().retainAll(ingredientsRecipes);
        recipe.getIngredientsRecipes().addAll(ingredientsRecipes);

        //recipe.setIngredientsRecipes(ingredientsRecipes);

        this.recipeRepository.delete(editedRecipe);
    }

    private void approveWaitingForApprovalRecipe(Recipe recipe) {
        recipe.setVisible(true);
        recipe.setModeratorComment("");
        recipe.setState(PUBLISHED);
        this.recipeRepository.save(recipe);
    }

    @Override
    public boolean moderatorDecline(UUID id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (recipe.getState()) {
            case WAITING_FOR_APPROVAL:
                return this.removeById(recipe.getId());

            case EDITED:
                this.declineEditedChanges(recipe);
                return true;


            case CHANGES_NEEDED:
            case DELETED:
            case PUBLISHED:
                return false;

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    private void declineEditedChanges(Recipe recipe) {
        Recipe editedRecipe = recipe.getClonedRef();

        recipe.setState(PUBLISHED);
        recipe.setClonedRef(null);

        this.recipeRepository.delete(editedRecipe);
        this.recipeRepository.save(recipe);
    }

    @Override
    public boolean moderatorRequestForChanges(UUID id, String message) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (recipe.getState()) {
            case WAITING_FOR_APPROVAL:
            case CHANGES_NEEDED:
                recipe.setModeratorComment(message);
                recipe.setState(CHANGES_NEEDED);
                this.recipeRepository.save(recipe);
                return true;

            case EDITED:
            case DELETED:
            case PUBLISHED:
                return false;

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    @Override
    public boolean moderatorCloneChanges(UUID id) {
        Recipe recipe = this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (recipe.getState()) {
            case EDITED:
                recipe.setState(PUBLISHED);
                Recipe editedRecipe = recipe.getClonedRef();
                recipe.setClonedRef(null);
                editedRecipe.setState(PUBLISHED);
                editedRecipe.setVisible(true);
                this.recipeRepository.save(recipe);
                this.recipeRepository.save(editedRecipe);
                return true;

            case CHANGES_NEEDED:
            case WAITING_FOR_APPROVAL:
            case DELETED:
            case PUBLISHED:
                return false;

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    @Override
    public Recipe update(RecipeWithStepsResource dto) {
        RecipeResource resource = dto.getInfo();
        List<RecipeStepResource> resourceSteps = dto.getSteps();
        Recipe oldRecipe = this.recipeRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        switch (oldRecipe.getState()) {
            case CHANGES_NEEDED:
                oldRecipe.setState(WAITING_FOR_APPROVAL);
                return this.updateDirectly(resource, resourceSteps, oldRecipe);

            case WAITING_FOR_APPROVAL:
                return this.updateDirectly(resource, resourceSteps, oldRecipe);

            case DELETED:
                throw new EntityDoesNotExistsException("Recipe");

            case EDITED:
            case PUBLISHED:
                return this.updateWithCloning(resource, resourceSteps, oldRecipe);

            default:
                throw new RuntimeException("Reached unreachable statement");
        }
    }

    private Recipe updateDirectly(RecipeResource resource, List<RecipeStepResource> resourceSteps, Recipe oldRecipe) {
        oldRecipe.setName(resource.getName());

        if (resource.getCalories() != null) {
            oldRecipe.setCalories(resource.getCalories() == 0 ? null : resource.getCalories());
        }

        if (resource.getFats() != null) {
            oldRecipe.setFats(resource.getFats() == 0 ? null : resource.getFats());
        }

        if (resource.getProteins() != null) {
            oldRecipe.setProteins(resource.getProteins() == 0 ? null : resource.getProteins());
        }

        if (resource.getCarbohydrates() != null) {
            oldRecipe.setCarbohydrates(resource.getCarbohydrates() == 0 ? null : resource.getCarbohydrates());
        }

        if (resource.getCookingMethods() != null) {
            oldRecipe.setCookingMethods(resource.getCookingMethods());
        }

        if (resource.getCookingTime() != null) {
            oldRecipe.setCookingTime(resource.getCookingTime());
        }

        if (resource.getPrice() != null) {
            oldRecipe.setPrice(resource.getPrice());
        }

        if (resource.getCuisine() != null) {
            oldRecipe.setCuisine(resource.getCuisine());
        }

        if (resource.getTags() != null) {
            oldRecipe.setTags(resource.getTags().stream()
                    .map(tagService::add).collect(Collectors.toSet()));
        }

        if (resource.getIngredients() != null) {
            Set<IngredientsRecipes> ingredients = updateRecipeIngredients(resource, oldRecipe);
            oldRecipe.getIngredientsRecipes().retainAll(ingredients);
            oldRecipe.getIngredientsRecipes().addAll(ingredients);
        }

        if (resourceSteps != null) {
            updateRecipeSteps(resourceSteps, oldRecipe);
        }

        return this.recipeRepository.save(oldRecipe);
    }

    private Recipe updateWithCloning(RecipeResource resource, List<RecipeStepResource> resourceSteps, Recipe oldRecipe) {
        Recipe clonedRecipe;

        if (oldRecipe.getClonedRef() != null) {
            clonedRecipe = oldRecipe.getClonedRef();
        } else {
            assert(oldRecipe.getState() != Recipe.State.EDITED);
            clonedRecipe = this.cloneRecipe(oldRecipe.getId(), oldRecipe.getOwner());
        }

        clonedRecipe.setOriginalRef(oldRecipe);
        oldRecipe.setState(Recipe.State.EDITED);
        clonedRecipe.setVisible(false);

        this.updateDirectly(resource, resourceSteps, clonedRecipe);

        return this.recipeRepository.save(oldRecipe);
    }

    private Set<IngredientsRecipes> updateRecipeIngredients(RecipeResource resource, Recipe recipe) {
        List<RecipeIngredientResource> recipeIngredientResources = resource.getIngredients();
        Set<IngredientsRecipes> ingredients = new HashSet<>();

        for (RecipeIngredientResource res : recipeIngredientResources) {
            Ingredient ingredient = ingredientService.findById(res.getId());
            IngredientsRecipes ingredientsRecipes = new IngredientsRecipes();

            ingredientsRecipes.setIngredient(ingredient);
            ingredientsRecipes.setRecipe(recipe);
            ingredientsRecipes.setValue(res.getValue());
            ingredientsRecipes.setValueType(res.getValueType());

            ingredients.add(ingredientsRecipes);
        }

        return ingredients;
    }

    private void updateRecipeSteps(List<RecipeStepResource> resourceSteps, Recipe oldRecipe) {
        List<RecipeStep> steps = oldRecipe.getSteps();
        Map<UUID, RecipeStep> stepMap = new LinkedHashMap<>();
        for (RecipeStep step : steps) {
            stepMap.put(step.getId(), step);
        }

        oldRecipe.setSteps(resourceSteps.stream().map(stepResource -> {
            RecipeStep step;
            if (stepResource.getId() != null) {
                if (stepMap.containsKey(stepResource.getId())) {
                    step = stepMap.get(stepResource.getId());
                } else {
                    throw new RequestParseException("Invalid step ID");
                }
            } else {
                step = new RecipeStep();
                step.setId(UUID.randomUUID());
                step.setRecipe(oldRecipe);
            }

            if (stepResource.getDescription() != null) {
                step.setDescription(stepResource.getDescription());
            }

            if (stepResource.getPicture() != null) {
                step.setPicture(stepResource.getPicture());
            }

            return step;
        }).collect(Collectors.toList()));
    }

    @Override
    public Recipe create(RecipeWithStepsResource dto, User owner) {
        RecipeResource resource = dto.getInfo();
        List<RecipeStepResource> steps = dto.getSteps();

        Recipe recipe = new Recipe();

        recipe.setId(UUID.randomUUID());

        recipe.setName(resource.getName());
        recipe.setCarbohydrates(resource.getCarbohydrates());
        recipe.setProteins(resource.getProteins());
        recipe.setCalories(resource.getCalories());
        recipe.setFats(resource.getFats());
        recipe.setRating(0f);
        recipe.setOwner(owner);
        recipe.setCuisine(resource.getCuisine());
        recipe.setCookingTime(resource.getCookingTime());
        recipe.setPrice(resource.getPrice());
        recipe.setReviewsNumber(0);

        // initial state
        recipe.setState(Recipe.State.WAITING_FOR_APPROVAL);
        recipe.setVisible(false);

        if (resource.getCookingMethods() != null) {
            recipe.setCookingMethods(resource.getCookingMethods());
        }

        if (resource.getTags() != null) {
            recipe.setTags(resource.getTags().stream()
                    .map(tagService::add).collect(Collectors.toSet()));
        } else {
            recipe.setTags(new HashSet<>());
        }

        if (steps == null) {
            throw new RequestParseException("Recipe must contain at least 1 step");
        }

        recipe.setSteps(steps.stream().map(recipeStepResource -> {
            RecipeStep step = new RecipeStep();

            step.setId(UUID.randomUUID());
            step.setDescription(recipeStepResource.getDescription());
            step.setPicture(recipeStepResource.getPicture());
            step.setRecipe(recipe);

            return step;
        }).collect(Collectors.toList()));

        Set<IngredientsRecipes> ingredients = updateRecipeIngredients(resource, recipe);
        recipe.setIngredientsRecipes(ingredients);

        return this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe cloneRecipe(UUID id, User user) {
        Recipe recipe = new Recipe(this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe")));
        recipe.setOwner(user);
        return this.recipeRepository.save(recipe);
    }

    public List<UserReview> findReviewsById(UUID id) {
        return recipeRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("receipt")).getReviews();
    }

    @Override
    public UserReview findReviewByIds(UUID receiptId, UUID reviewId) {
        List<UserReview> reviews = recipeRepository.findById(receiptId).orElseThrow(() -> new EntityDoesNotExistsException("receipt"))
                .getReviews().stream().filter(rev -> rev.getId().equals(reviewId)).collect(Collectors.toList());
        if (reviews.size() == 0)
            throw new EntityDoesNotExistsException("review");
        return reviews.get(0);
    }

    @Override
    public UserReview addReview(UUID recipeId, UserReviewResource userReviewResource) {
        UserReview review = new UserReview();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));
        User user = securityAccessResolver.getUser();
        if (recipe.getReviews().stream().anyMatch(rev -> rev.getUser().getId().equals(user.getId()))) {
            throw new AlreadyExistsException("Review", "author");
        }
        review.setId(UUID.randomUUID());
        review.setUser(user);
        review.setRecipe(recipe);
        review.setCreated_on(new Date());
        review.setRating(userReviewResource.getRating());
        review.setReview(userReviewResource.getReview());
        recipe.getReviews().add(review);
        recipe.setReviewsNumber(recipe.getReviewsNumber() + 1);
        if (recipe.getReviewsNumber() == 1) {
            recipe.setRating(review.getRating());
        } else {
            recipe.setRating((recipe.getRating() * (recipe.getReviewsNumber() - 1) + review.getRating()) / recipe.getReviewsNumber());
        }
        recipeRepository.save(recipe);
        return review;
    }

    @Override
    public Page<Recipe> search(
            RecipeSearchCriteria recipeSearchCriteria,
            Pageable pageable
    ) {
        Set<Tag> includeTags = new HashSet<>();
        Set<Tag> excludeTags = new HashSet<>();

        if (recipeSearchCriteria.getIncludeTags() != null) {
            includeTags.addAll(recipeSearchCriteria.getIncludeTags().stream().map(s -> {
                if (tagService.existsByName(s)) {
                    return tagService.findByName(s);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        if (recipeSearchCriteria.getExcludeTags() != null) {
            excludeTags.addAll(recipeSearchCriteria.getExcludeTags().stream().map(s -> {
                if (tagService.existsByName(s)) {
                    return tagService.findByName(s);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        Set<Ingredient> includeIngredients = new HashSet<>();
        Set<Ingredient> excludeIngredients = new HashSet<>();

        if (recipeSearchCriteria.getIncludeIngredients() != null) {
            includeIngredients.addAll(recipeSearchCriteria.getIncludeIngredients().stream().map(ingredient -> {
                if (ingredientService.existsById(ingredient)) {
                    return ingredientService.findById(ingredient);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        if (recipeSearchCriteria.getExcludeIngredients() != null) {
            excludeIngredients.addAll(recipeSearchCriteria.getExcludeIngredients().stream().map(ingredient -> {
                if (ingredientService.existsById(ingredient)) {
                    return ingredientService.findById(ingredient);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        return this.recipeRepository.findAll(
                new RecipeSearchSpecification(
                        recipeSearchCriteria,
                        includeTags,
                        excludeTags,
                        includeIngredients,
                        excludeIngredients
                ),
                pageable
        );
    }
}