package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.RecipeSearchCriteria;
import com.ncedu.nc_edu.dto.resources.RecipeWithStepsResource;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    List<Recipe> findByName(String name);
    Recipe findById(UUID id);
    Page<Recipe> findAll(Pageable pageable);
    List<Recipe> findAllOwn(User user);
    void removeById(UUID id);
    Recipe update(RecipeWithStepsResource dto);
    Recipe create(RecipeWithStepsResource dto, User owner);
    Recipe cloneRec(UUID id, User user);
    Page<Recipe> search(RecipeSearchCriteria recipeSearchCriteria, Pageable pageable);
}
