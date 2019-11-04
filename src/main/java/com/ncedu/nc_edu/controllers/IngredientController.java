package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.exceptions.IngredientDoesNotExist;
import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Controller
public class IngredientController {
    private IngredientService ingredientService;

    IngredientController(@Autowired IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping(value = "/ingredients")
    @ResponseBody
    public List<Ingredient> getAllIngredients() {
        return ingredientService.findAll();
    }

    @GetMapping(value = "/ingredients/{id}")
    @ResponseBody
    public Ingredient getIngredientById(@PathVariable UUID id) {
        try {
            return ingredientService.findById(id);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such ingredient");
        }
    }

    @PutMapping(value = "/ingredients")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Ingredient updateIngredient(Ingredient ingredient) {
        try {
            return ingredientService.update(ingredient);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such ingredient");
        }
    }
}
