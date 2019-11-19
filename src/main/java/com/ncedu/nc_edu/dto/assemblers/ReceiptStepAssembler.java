package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.dto.resources.ReceiptStepResource;
import com.ncedu.nc_edu.models.ReceiptStep;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ReceiptStepAssembler extends RepresentationModelAssemblerSupport<ReceiptStep, ReceiptStepResource> {
    public ReceiptStepAssembler() {
        super(ReceiptStep.class, ReceiptStepResource.class);
    }

    @Override
    public ReceiptStepResource toModel(ReceiptStep entity) {
        ReceiptStepResource resource = new ReceiptStepResource();

        resource.setId(entity.getId());
        resource.setDescription(entity.getDescription());
        resource.setPicture(entity.getPicture());

        return resource;
    }
}
