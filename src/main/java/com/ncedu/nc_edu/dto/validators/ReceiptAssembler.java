package com.ncedu.nc_edu.dto.validators;

import com.ncedu.nc_edu.dto.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ReceiptAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptResource> {
    public ReceiptAssembler() {
        super(Receipt.class, ReceiptResource.class);
    }

    @Override
    public ReceiptResource toModel(Receipt entity) {
        ReceiptResource resource = new ReceiptResource();
        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setCalories(entity.getCalories());
        resource.setProteins(entity.getProteins());
        resource.setCarbohydrates(entity.getCarbohydrates());
        resource.setFats(entity.getFats());
        resource.setRating(entity.getRating());
        return resource;
    }
}
