package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.exceptions.IngredientDoesNotExist;
import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class IngredientController {
    private IngredientService ingredientService;

    IngredientController(@Autowired IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping(value = "/ingredients")
    public List<Ingredient> getAll(@RequestParam(required = false) String name) {
        if (name != null)
            return ingredientService.findByName(name);
        return ingredientService.findAll();
    }

    @GetMapping(value = "/ingredients/{id}")
    public Ingredient getById(@PathVariable UUID id) {
        try {
            return ingredientService.findById(id);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such an ingredient");
        }
    }

    @PostMapping(value = "/ingredients")
    public Ingredient add(@RequestParam @NotNull String name) {
        return ingredientService.add(name);
    }

    @PutMapping(value = "/ingredients/{id}")
    public Ingredient update(@PathVariable UUID id, @RequestBody Ingredient ingredient) {
        try {
            return ingredientService.update(ingredient);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no ingredient with the given id");
        }
    }
}
