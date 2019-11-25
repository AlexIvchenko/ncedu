package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.ReceiptController;
import com.ncedu.nc_edu.dto.resources.ReceiptStepResource;
import com.ncedu.nc_edu.models.ReceiptStep;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        resource.add(linkTo(methodOn(ReceiptController.class).getReceiptSteps(entity.getId())).withRel("receipt"));

        return resource;
    }
}
