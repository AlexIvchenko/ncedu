package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.exceptions.IngredientDoesNotExist;
import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.services.IngredientService;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping(value = "/ingredients")//, produces={"application/json; charset=UTF-8"})
    public List<Ingredient> getAllIngredients(@RequestParam(required = false) String name) {
        if (name != null)
            return ingredientService.findIngredientsByName(name);
        return ingredientService.findAll();
    }

    @GetMapping(value = "/ingredients/{id}")
    public Ingredient getIngredientById(@PathVariable UUID id) {
        try {
            return ingredientService.findById(id);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such an ingredient");
        }
    }

    @PostMapping(value = "/ingredients")
    public Ingredient addIngredient(@RequestParam @NotNull String name) {
        return ingredientService.addIngredient(name);
    }

    @PutMapping(value = "/ingredients/{id}")
    public Ingredient updateIngredient(@PathVariable UUID id, @RequestBody Ingredient ingredient) {
        try {
            Ingredient oldIngredient = ingredientService.findById(id);
            return ingredientService.updateIngredient(ingredient);
        } catch (IngredientDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no ingredient with the given id");
        }
    }
}
