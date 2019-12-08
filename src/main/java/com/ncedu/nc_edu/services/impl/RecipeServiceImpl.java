package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.*;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.*;
import com.ncedu.nc_edu.repositories.RecipeRepository;
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

@Service
@Transactional
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final TagService tagService;
    private final IngredientService ingredientService;

    public RecipeServiceImpl(
            @Autowired RecipeRepository recipeRepository,
            @Autowired TagService tagService,
            @Autowired IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
    }

    public Page<Recipe> findAll(Pageable pageable) {
        return this.recipeRepository.findAll(pageable);
    }

    public Recipe findById(UUID id) {
        return this.recipeRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));
    }

    public List<Recipe> findByName(String name) {
        return this.recipeRepository.findByNameContaining(name);
    }

    @Override
    public List<Recipe> findAllOwn(User user) {
        return this.recipeRepository.findByOwner(user);
    }

    @Override
    public void removeById(UUID id) {
        this.recipeRepository.deleteById(id);
    }

    @Override
    public Recipe update(RecipeWithStepsResource dto) {
        RecipeResource resource = dto.getInfo();
        List<RecipeStepResource> resourceSteps = dto.getSteps();
        Recipe oldRecipe = this.recipeRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityDoesNotExistsException("Recipe"));

        if (resource.getName() != null) {
            oldRecipe.setName(resource.getName());
        }

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

        if (resource.getCookingMethod() != null) {
            oldRecipe.setCookingMethod(Recipe.CookingMethod.valueOf(resource.getCookingMethod()));
        }

        if (resource.getCookingTime() != null) {
            oldRecipe.setCookingTime(resource.getCookingTime());
        }

        if (resource.getPrice() != null) {
            oldRecipe.setPrice(resource.getPrice());
        }

        if (resource.getCuisine() != null) {
            oldRecipe.setCuisine(Recipe.Cuisine.valueOf(resource.getCuisine()));
        }

        if (resource.getTags() != null) {
            oldRecipe.setTags(resource.getTags().stream()
                    .map(tagService::findByName).collect(Collectors.toSet()));
        }

        if (resource.getIngredients() != null) {
            List<RecipeIngredientResource> recipeIngredientResources = resource.getIngredients();
            Set<IngredientsRecipes> ingredients = new HashSet<>();

            for (RecipeIngredientResource res : recipeIngredientResources) {
                Ingredient ingredient = ingredientService.findById(res.getId());

                IngredientsRecipes ingredientsRecipes = new IngredientsRecipes();
                ingredientsRecipes.setIngredient(ingredient);
                ingredientsRecipes.setRecipe(oldRecipe);
                ingredientsRecipes.setValue(res.getValue());
                ingredientsRecipes.setValueType(res.getValueType());

                ingredients.add(ingredientsRecipes);
            }

            oldRecipe.getIngredientsRecipes().retainAll(ingredients);
            oldRecipe.getIngredientsRecipes().addAll(ingredients);
        }

        if (resourceSteps != null) {
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

        return this.recipeRepository.save(oldRecipe);
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
        recipe.setCuisine(Recipe.Cuisine.valueOf(resource.getCuisine()));
        recipe.setCookingMethod(Recipe.CookingMethod.valueOf(resource.getCookingMethod()));
        recipe.setCookingTime(resource.getCookingTime());
        recipe.setPrice(resource.getPrice());

        if (resource.getTags() != null) {
            recipe.setTags(resource.getTags().stream()
                    .map(tagService::findByName).collect(Collectors.toSet()));
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

        recipe.setIngredientsRecipes(ingredients);

        return this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe cloneRec(UUID id, User user) {
        Recipe receipt = new Recipe(this.recipeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("Receipt")));
        receipt.setOwner(user);
        return this.recipeRepository.save(receipt);
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