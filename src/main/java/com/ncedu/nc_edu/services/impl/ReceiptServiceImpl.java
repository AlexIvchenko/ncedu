package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.dto.resources.ReceiptSearchCriteria;
import com.ncedu.nc_edu.dto.resources.ReceiptStepResource;
import com.ncedu.nc_edu.dto.resources.ReceiptWithStepsResource;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.*;
import com.ncedu.nc_edu.repositories.ReceiptRepository;
import com.ncedu.nc_edu.services.IngredientService;
import com.ncedu.nc_edu.services.ReceiptService;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final TagService tagService;
    private final IngredientService ingredientService;

    public ReceiptServiceImpl(
            @Autowired ReceiptRepository receiptRepository,
            @Autowired TagService tagService,
            @Autowired IngredientService ingredientService) {
        this.receiptRepository = receiptRepository;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
    }

    public Page<Receipt> findAll(Pageable pageable) {
        return this.receiptRepository.findAll(pageable);
    }

    public Receipt findById(UUID id) {
        return this.receiptRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("Receipt"));
    }

    public List<Receipt> findByName(String name) {
        return this.receiptRepository.findByNameContaining(name);
    }

    @Override
    public List<Receipt> findAllOwn(User user) {
        return this.receiptRepository.findByOwner(user);
    }

    @Override
    public void removeById(UUID id) {
        this.receiptRepository.deleteById(id);
    }

    @Override
    public Receipt update(ReceiptWithStepsResource dto) {
        ReceiptResource resource = dto.getInfo();
        List<ReceiptStepResource> resourceSteps = dto.getSteps();
        Receipt oldReceipt = this.receiptRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityDoesNotExistsException("Receipt"));

        if (resource.getName() != null) {
            oldReceipt.setName(resource.getName());
        }

        if (resource.getCalories() != null) {
            oldReceipt.setCalories(resource.getCalories() == 0 ? null : resource.getCalories());
        }

        if (resource.getFats() != null) {
            oldReceipt.setFats(resource.getFats() == 0 ? null : resource.getFats());
        }

        if (resource.getProteins() != null) {
            oldReceipt.setProteins(resource.getProteins() == 0 ? null : resource.getProteins());
        }

        if (resource.getCarbohydrates() != null) {
            oldReceipt.setCarbohydrates(resource.getCarbohydrates() == 0 ? null : resource.getCarbohydrates());
        }

        if (resource.getCookingMethod() != null) {
            oldReceipt.setCookingMethod(Receipt.CookingMethod.valueOf(resource.getCookingMethod()));
        }

        if (resource.getCookingTime() != null) {
            oldReceipt.setCookingTime(resource.getCookingTime());
        }

        if (resource.getPrice() != null) {
            oldReceipt.setPrice(resource.getPrice());
        }

        if (resource.getCuisine() != null) {
            oldReceipt.setCuisine(Receipt.Cuisine.valueOf(resource.getCuisine()));
        }

        if (resource.getTags() != null) {
            oldReceipt.setTags(resource.getTags().stream()
                    .map(tagService::findByName).collect(Collectors.toSet()));
        }

        if (resourceSteps != null) {
            List<ReceiptStep> steps = oldReceipt.getSteps();
            Map<UUID, ReceiptStep> stepMap = new LinkedHashMap<>();
            for (ReceiptStep step : steps) {
                stepMap.put(step.getId(), step);
            }

            oldReceipt.setSteps(resourceSteps.stream().map(stepResource -> {
                ReceiptStep step;
                if (stepResource.getId() != null) {
                    if (stepMap.containsKey(stepResource.getId())) {
                        step = stepMap.get(stepResource.getId());
                    } else {
                        throw new RequestParseException("Invalid step ID");
                    }
                } else {
                    step = new ReceiptStep();
                    step.setId(UUID.randomUUID());
                    step.setReceipt(oldReceipt);
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

        return this.receiptRepository.save(oldReceipt);
    }

    @Override
    public Receipt create(ReceiptWithStepsResource dto, User owner) {
        ReceiptResource resource = dto.getInfo();
        List<ReceiptStepResource> steps = dto.getSteps();

        Receipt receipt = new Receipt();

        receipt.setId(UUID.randomUUID());

        receipt.setName(resource.getName());
        receipt.setCarbohydrates(resource.getCarbohydrates());
        receipt.setProteins(resource.getProteins());
        receipt.setCalories(resource.getCalories());
        receipt.setFats(resource.getFats());
        receipt.setRating(0f);
        receipt.setOwner(owner);
        receipt.setCuisine(Receipt.Cuisine.valueOf(resource.getCuisine()));
        receipt.setCookingMethod(Receipt.CookingMethod.valueOf(resource.getCookingMethod()));
        receipt.setCookingTime(resource.getCookingTime());
        receipt.setPrice(resource.getPrice());

        if (resource.getTags() != null) {
            receipt.setTags(resource.getTags().stream()
                    .map(tagService::findByName).collect(Collectors.toSet()));
        }

        if (steps == null) {
            throw new RequestParseException("Receipt must contain at least 1 step");
        }

        receipt.setSteps(steps.stream().map(receiptStepResource -> {
            ReceiptStep step = new ReceiptStep();
            step.setId(UUID.randomUUID());
            step.setDescription(receiptStepResource.getDescription());
            step.setPicture(receiptStepResource.getPicture());
            step.setReceipt(receipt);
            return step;
        }).collect(Collectors.toList()));

        List<JSONObject> json = dto.getInfo().getIngredients();
        Set<IngredientsReceipts> ingredients = new HashSet<>();

        for (JSONObject j : json) {
            if (j.containsKey("id")) {
                UUID id;
                String valueType;
                Float value;

                try {
                    id = UUID.fromString((String) j.get("id"));
                    value = Float.parseFloat(j.getAsString("value"));
                    valueType = (String) j.get("valueType");
                } catch (NumberFormatException ex) {
                    throw new RequestParseException("Invalid ingredient value format");
                } catch (IllegalArgumentException ex) {
                    throw new RequestParseException("Invalid ingredient id");
                } catch (ClassCastException ex) {
                    throw new RequestParseException("Invalid ingredients format");
                }

                if (valueType == null) {
                    throw new RequestParseException("Invalid ingredients format");
                }

                Ingredient ingredient = ingredientService.findById(id);

                IngredientsReceipts ingredientsReceipts = new IngredientsReceipts();
                ingredientsReceipts.setIngredient(ingredient);
                ingredientsReceipts.setReceipt(receipt);
                ingredientsReceipts.setValue(value);
                ingredientsReceipts.setValueType(valueType);

                ingredients.add(ingredientsReceipts);
            } else {
                throw new RequestParseException("Invalid ingredients format");
            }
        }

        receipt.setIngredientsReceipts(ingredients);

        return this.receiptRepository.save(receipt);
    }

    @Override
    public Page<Receipt> search(
            ReceiptSearchCriteria receiptSearchCriteria,
            Pageable pageable
    ) {
        Set<Tag> includeTags = new HashSet<>();
        Set<Tag> excludeTags = new HashSet<>();

        if (receiptSearchCriteria.getIncludeTags() != null) {
            includeTags.addAll(receiptSearchCriteria.getIncludeTags().stream().map(s -> {
                if (tagService.existsByName(s)) {
                    return tagService.findByName(s);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        if (receiptSearchCriteria.getExcludeTags() != null) {
            excludeTags.addAll(receiptSearchCriteria.getExcludeTags().stream().map(s -> {
                if (tagService.existsByName(s)) {
                    return tagService.findByName(s);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        Set<Ingredient> includeIngredients = new HashSet<>();
        Set<Ingredient> excludeIngredients = new HashSet<>();

        if (receiptSearchCriteria.getIncludeIngredients() != null) {
            includeIngredients.addAll(receiptSearchCriteria.getIncludeIngredients().stream().map(ingredient -> {
                if (ingredientService.existsById(ingredient)) {
                    return ingredientService.findById(ingredient);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        if (receiptSearchCriteria.getExcludeIngredients() != null) {
            excludeIngredients.addAll(receiptSearchCriteria.getExcludeIngredients().stream().map(ingredient -> {
                if (ingredientService.existsById(ingredient)) {
                    return ingredientService.findById(ingredient);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        }

        return this.receiptRepository.findAll(
                new ReceiptSearchSpecification(
                        receiptSearchCriteria,
                        includeTags,
                        excludeTags,
                        includeIngredients,
                        excludeIngredients
                ),
                pageable
        );
    }
}