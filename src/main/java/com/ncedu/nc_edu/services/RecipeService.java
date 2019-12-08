package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.RecipeSearchCriteria;
import com.ncedu.nc_edu.dto.resources.RecipeWithStepsResource;
import com.ncedu.nc_edu.dto.resources.UserReviewResource;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    List<Recipe> findByName(String name);
    Recipe findById(UUID id);
    List<UserReview> findReviewsById(UUID id);
    UserReview findReviewByIds(UUID receiptId, UUID reviewId);
    UserReview addReview(UUID recipeId, UserReviewResource userReviewResource);
    Page<Recipe> findAll(Pageable pageable);
    List<Recipe> findAllOwn(User user);
    void removeById(UUID id);
    Recipe update(RecipeWithStepsResource dto);
    Recipe create(RecipeWithStepsResource dto, User owner);
    Page<Recipe> search(RecipeSearchCriteria recipeSearchCriteria, Pageable pageable);
}
