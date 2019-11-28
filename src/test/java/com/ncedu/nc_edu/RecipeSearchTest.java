package com.ncedu.nc_edu;

import com.ncedu.nc_edu.dto.resources.RecipeSearchCriteria;
import com.ncedu.nc_edu.models.Recipe;
import com.ncedu.nc_edu.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Slf4j
@SpringBootTest
public class RecipeSearchTest {
    @Test
    public void searchTest(@Autowired RecipeService recipeServices) {
        RecipeSearchCriteria criteria = new RecipeSearchCriteria();

        criteria.setCaloriesMin(100);
        criteria.setIncludeTags(Set.of("Tag1"));
        criteria.setExcludeTags(Set.of("Tag3"));
        criteria.setFatsMin(40f);

        Page<Recipe> recipes = recipeServices.search(criteria, Pageable.unpaged());

        recipes.stream().forEach(recipe -> log.info(recipe.getName()));

        assert(true);
    }
}
