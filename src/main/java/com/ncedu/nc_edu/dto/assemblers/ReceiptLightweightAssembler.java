package com.ncedu.nc_edu.dto.assemblers;


import com.ncedu.nc_edu.dto.resources.ReceiptLightweightResource;
import com.ncedu.nc_edu.models.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReceiptLightweightAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptLightweightResource> {
    private final TagAssembler tagAssembler;

    public ReceiptLightweightAssembler(@Autowired TagAssembler tagAssembler) {
        super(Receipt.class, ReceiptLightweightResource.class);
        this.tagAssembler =tagAssembler;
    }

    @Override
    public ReceiptLightweightResource toModel(Receipt entity) {
        ReceiptLightweightResource resource = new ReceiptLightweightResource();

        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCalories(entity.getCalories());
        resource.setFats(entity.getFats());
        resource.setCarbohydrates(entity.getCarbohydrates());
        resource.setProteins(entity.getProteins());
        resource.setRating(entity.getRating());
        resource.setOwner(entity.getOwner().getId());

        resource.setTags(entity.getTags().stream()
                .map(tagAssembler::toModel).collect(Collectors.toSet())
        );

        return resource;
    }
}
