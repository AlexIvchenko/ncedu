package com.ncedu.nc_edu.repositories;

import com.ncedu.nc_edu.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    Ingredient findByName(String name);
    Ingredient findByNameLike(String pattern);
}
