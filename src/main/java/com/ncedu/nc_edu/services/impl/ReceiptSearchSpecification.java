package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.ReceiptSearchCriteria;
import com.ncedu.nc_edu.models.Ingredient;
import com.ncedu.nc_edu.models.IngredientsReceipts;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;
import java.util.stream.Collectors;

public class ReceiptSearchSpecification implements Specification<Receipt> {
    private ReceiptSearchCriteria criteria;
    private Set<Tag> includeTags, excludeTags;
    private Set<Ingredient> includeIngredients, excludeIngredients;

    public ReceiptSearchSpecification(
            ReceiptSearchCriteria criteria,
            Set<Tag> includeTags,
            Set<Tag> excludeTags,
            Set<Ingredient> includeIngredients,
            Set<Ingredient> excludeIngredients
    ) {
        this.criteria = criteria;
        this.includeTags = includeTags;
        this.excludeTags = excludeTags;
        this.includeIngredients = includeIngredients;
        this.excludeIngredients = excludeIngredients;
    }

    @Override
    public Predicate toPredicate(Root<Receipt> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
        return criteriaBuilder.and(fill(root, query, criteriaBuilder).toPredicate(root, query, criteriaBuilder));
}

    private Specification<Receipt> fill(Root<Receipt> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
        Specification<Receipt> spec = (root, query, criteriaBuilder) -> null;

        if (criteria.getName() != null) {
            spec = spec.and(containsName(criteria.getName()));
        }

        if (criteria.getCaloriesMin() != null) {
            spec = spec.and(caloriesGreaterThanOrEqualTo(criteria.getCaloriesMin()));
        }

        if (criteria.getCaloriesMax() != null) {
            spec = spec.and(caloriesLessThanOrEqualTo(criteria.getCaloriesMax()));
        }

        if (criteria.getFatsMin() != null) {
            spec = spec.and(fatsGreaterThanOrEqualTo(criteria.getFatsMin()));
        }

        if (criteria.getFatsMax() != null) {
            spec = spec.and(fatsLessThanOrEqualTo(criteria.getFatsMax()));
        }

        if (criteria.getCarbohydratesMin() != null) {
            spec = spec.and(carbohydratesGreaterThanOrEqualTo(criteria.getCarbohydratesMin()));
        }

        if (criteria.getCarbohydratesMax() != null) {
            spec = spec.and(carbohydratesLessThanOrEqualTo(criteria.getCarbohydratesMax()));
        }

        if (criteria.getProteinsMin() != null) {
            spec = spec.and(proteinsGreaterThanOrEqualTo(criteria.getProteinsMin()));
        }

        if (criteria.getProteinsMax() != null) {
            spec = spec.and(proteinsLessThanOrEqualTo(criteria.getProteinsMax()));
        }

        if (criteria.getRatingMin() != null) {
            spec = spec.and(ratingGreaterThanOrEqualTo(criteria.getRatingMin()));
        }

        if (criteria.getRatingMax() != null) {
            spec = spec.and(ratingLessThanOrEqualTo(criteria.getRatingMax()));
        }

        if (criteria.getCookingMethods() != null) {
            if (criteria.getCookingMethods().size() != 0) {
                spec = spec.and(inCookingMethods(
                        criteria.getCookingMethods().stream().map(Receipt.CookingMethod::valueOf).collect(Collectors.toSet()))
                );
            }
        }

        if (criteria.getCookingTimeMin() != null) {
            spec = spec.and(cookingTimeGreaterThanOrEqualTo(criteria.getCookingTimeMin()));
        }

        if (criteria.getCookingTimeMax() != null) {
            spec = spec.and(cookingTimeLessThanOrEqualTo(criteria.getCookingTimeMax()));
        }

        if (criteria.getPriceMin() != null) {
            spec = spec.and(priceGreaterThanOrEqualTo(criteria.getPriceMin()));
        }

        if (criteria.getPriceMax() != null) {
            spec = spec.and(priceLessThanOrEqualTo(criteria.getPriceMax()));
        }

        if (criteria.getCuisines() != null) {
            if (criteria.getCuisines().size() != 0) {
                spec = spec.and(inCuisines(criteria.getCuisines()
                        .stream().map(Receipt.Cuisine::valueOf).collect(Collectors.toSet()))
                );
            }
        }

        if (this.includeTags.size() > 0) {
            spec = spec.and(containTags(this.includeTags));
        }

        if (this.excludeTags.size() > 0) {
            spec = spec.and(notContainTags(this.excludeTags));
        }

        if (this.includeIngredients.size() > 0) {
            spec = spec.and(containIngredients(this.includeIngredients));
        }

        if (this.excludeIngredients.size() > 0) {
            spec = spec.and(notContainIngredients(this.excludeIngredients));
        }

        return spec;
    }

    private Specification<Receipt> caloriesGreaterThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("calories"), value);
    }

    private Specification<Receipt> caloriesLessThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("calories"), value);
    }

    private Specification<Receipt> fatsLessThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("fats"), value);
    }

    private Specification<Receipt> fatsGreaterThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("fats"), value);
    }

    private Specification<Receipt> carbohydratesLessThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("carbohydrates"), value);
    }

    private Specification<Receipt> carbohydratesGreaterThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("carbohydrates"), value);
    }

    private Specification<Receipt> proteinsLessThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("proteins"), value);
    }

    private Specification<Receipt> proteinsGreaterThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("proteins"), value);
    }

    private Specification<Receipt> ratingGreaterThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), value);
    }

    private Specification<Receipt> ratingLessThanOrEqualTo(Float value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("rating"), value);
    }

    private Specification<Receipt> containsName(String value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), value);
    }

    private Specification<Receipt> inCookingMethods(Set<Receipt.CookingMethod> methods) {
        return (root, query, criteriaBuilder) ->
                root.get("cookingMethod").in(methods);
    }

    private Specification<Receipt> cookingTimeGreaterThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("cookingTime"), value);
    }

    private Specification<Receipt> cookingTimeLessThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("cookingTime"), value);
    }

    private Specification<Receipt> priceGreaterThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
    }

    private Specification<Receipt> priceLessThanOrEqualTo(Integer value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
    }

    private Specification<Receipt> inCuisines(Set<Receipt.Cuisine> cuisines) {
        return (root, query, criteriaBuilder) ->
                root.get("cuisine").in(cuisines);
    }

    private Specification<Receipt> containTags(Set<Tag> tags) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Receipt> sq = query.subquery(Receipt.class);
            Root<Receipt> receipt = sq.from(Receipt.class);
            SetJoin<Receipt, Tag> receiptTagSetJoin = receipt.joinSet("tags", JoinType.INNER);

            return root.in(
                        sq.select(receipt).groupBy(receipt.get("id"))
                        .having(criteriaBuilder.equal(
                                    criteriaBuilder.count(receipt), tags.size()
                                )
                        ).where(receiptTagSetJoin.in(tags))
                    );
        };
    }

    private Specification<Receipt> notContainTags(Set<Tag> tags) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Receipt> sq = query.subquery(Receipt.class);
            Root<Receipt> receipt = sq.from(Receipt.class);
            SetJoin<Receipt, Tag> receiptTagSetJoin = receipt.joinSet("tags", JoinType.INNER);

            return criteriaBuilder.not(root.in(
                    sq.select(receipt).groupBy(receipt.get("id"))
                            .having(criteriaBuilder.equal(
                                    criteriaBuilder.count(receipt), tags.size()
                                    )
                            ).where(receiptTagSetJoin.in(tags))
            ));
        };
    }

    private Specification<Receipt> containIngredients(Set<Ingredient> ingredients) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Receipt> sq = query.subquery(Receipt.class);
            Root<Receipt> receipt = sq.from(Receipt.class);
            SetJoin<Receipt, IngredientsReceipts> ingredientsReceipts
                    = receipt.joinSet("ingredientsReceiptsDTOs", JoinType.INNER);
            Join<IngredientsReceipts, Ingredient> ingredientsJoin
                    = ingredientsReceipts.join("ingredient", JoinType.INNER);

            return root.in(
                    sq.select(receipt).groupBy(receipt.get("id"))
                            .having(criteriaBuilder.equal(
                                    criteriaBuilder.count(receipt), ingredients.size()
                                    )
                            ).where(ingredientsJoin.in(ingredients))
            );
        };
    }

    private Specification<Receipt> notContainIngredients(Set<Ingredient> ingredients) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Receipt> sq = query.subquery(Receipt.class);
            Root<Receipt> receipt = sq.from(Receipt.class);
            SetJoin<Receipt, IngredientsReceipts> ingredientsReceipts
                    = receipt.joinSet("ingredientsReceiptsDTOs", JoinType.INNER);
            Join<IngredientsReceipts, Ingredient> ingredientsJoin
                    = ingredientsReceipts.join("ingredient", JoinType.INNER);

            return criteriaBuilder.not(root.in(
                    sq.select(receipt).groupBy(receipt.get("id"))
                            .having(criteriaBuilder.equal(
                                    criteriaBuilder.count(receipt), ingredients.size()
                                    )
                            ).where(ingredientsJoin.in(ingredients))
            ));
        };
    }
}
