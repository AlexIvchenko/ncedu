package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.models.Receipt;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ReceiptSearchSpecification implements Specification<Receipt> {
    @Override
    public Predicate toPredicate(Root<Receipt> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
