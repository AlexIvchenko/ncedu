package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.ReceiptController;
import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
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
public class ReceiptAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptResource> {
    private final SecurityUtils securityUtils;
    private final IngredientAssembler ingredientAssembler;

    public ReceiptAssembler(@Autowired SecurityUtils securityUtils,
                            @Autowired IngredientAssembler ingredientAssembler) {
        super(Receipt.class, ReceiptResource.class);
        this.securityUtils = securityUtils;
        this.ingredientAssembler = ingredientAssembler;
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

        resource.setIngredients(entity.getIngredientsReceiptsDTOs().stream()
                .map(ingredientReceipt -> {
                    JSONObject json = new JSONObject();
                    json.put("id", ingredientReceipt.getIngredient().getId());
                    json.put("name", ingredientReceipt.getIngredient().getName());
                    json.put("valueType", ingredientReceipt.getValueType());
                    json.put("value", ingredientReceipt.getValue());
                    return json;
                }).collect(Collectors.toList())
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
