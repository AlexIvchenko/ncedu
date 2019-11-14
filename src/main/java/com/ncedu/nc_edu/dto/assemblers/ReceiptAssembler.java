package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReceiptAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptResource> {
    private final TagAssembler tagAssembler;
    private final StepAssembler stepAssembler;

    public ReceiptAssembler(@Autowired TagAssembler tagAssembler, @Autowired StepAssembler stepAssembler) {
        super(Receipt.class, ReceiptResource.class);
        this.tagAssembler = tagAssembler;
        this.stepAssembler = stepAssembler;
    }

    @Override
    public ReceiptResource toModel(Receipt entity) {
        ReceiptResource resource = new ReceiptResource();

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

        resource.setSteps(entity.getSteps().stream()
                .map(stepAssembler::toModel).collect(Collectors.toList())
        );

        return resource;
    }
}