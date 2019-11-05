package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.exceptions.IngredientDoesNotExist;
import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.repositories.IngredientRepository;
import com.ncedu.nc_edu.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(@Autowired IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient findById(UUID id) throws IngredientDoesNotExist {
        return ingredientRepository.findById(id).orElseThrow(IngredientDoesNotExist::new);
    }

    public Ingredient updateIngredient(Ingredient ingredient) throws IngredientDoesNotExist {
        Ingredient oldIngredient = ingredientRepository.findById(ingredient.getId())
                        .orElseThrow(IngredientDoesNotExist::new);
        if (ingredient.getName() != null)
            oldIngredient.setName(ingredient.getName());
        return ingredientRepository.save(oldIngredient);
    }

    public List<Ingredient> findIngredientsByName(String pattern) {
        return ingredientRepository.findByNameContainsIgnoreCase(pattern);
    }

    public Ingredient addIngredient(String name) {
        //unique?
        //if (ingredientRepository.findByName(name) != null) throw new IngredientAlreadyExist();
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setId(UUID.randomUUID());
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> findAll() {
        return new ArrayList<>(ingredientRepository.findAll());
    }
}
