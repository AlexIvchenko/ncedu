package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.exceptions.IngredientDoesNotExist;
import com.ncedu.nc_edu.models.Ingredient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public interface IngredientService {
    Ingredient findById(UUID id) throws IngredientDoesNotExist;
    Ingredient update(Ingredient ingredient) throws IngredientDoesNotExist;
    List<Ingredient> findAll();
}
