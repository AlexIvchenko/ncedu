package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.ReceiptResource;
import com.ncedu.nc_edu.dto.validators.ReceiptAssembler;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ReceiptController {
    private final ReceiptService receiptService;
    private final ReceiptAssembler receiptAssembler;

    public ReceiptController(@Autowired ReceiptService receiptService,
                             @Autowired ReceiptAssembler receiptAssembler) {
        this.receiptService = receiptService;
        this.receiptAssembler = receiptAssembler;
    }

    @GetMapping("/receipts")
    public List<ReceiptResource> getAll() {
        return this.receiptService.findAll().stream().map(receiptAssembler::toModel)
                .peek(receiptResource -> {
                    receiptResource.add(linkTo(methodOn(ReceiptController.class).getById(receiptResource.getId())).withSelfRel());
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/receipts/{id}")
    public ReceiptResource getById(@PathVariable UUID id) {
        ReceiptResource receipt = receiptAssembler.toModel(receiptService.findById(id));
        receipt.add(linkTo(methodOn(ReceiptController.class).getById(receipt.getId())).withSelfRel());
        return  receipt;
    }
}
