package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class IngredientController {
    private final IngredientService ingredientService;

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
        return ingredientService.findById(id);
    }

    @PostMapping(value = "/ingredients")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Ingredient add(@RequestParam @NotNull String name) {
        return ingredientService.add(name);
    }

    @PutMapping(value = "/ingredients/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Ingredient update(@PathVariable UUID id, @RequestBody Ingredient ingredient) {
        return ingredientService.update(ingredient);
    }
}
