package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.resources.ReceiptWithStepsResource;
import com.ncedu.nc_edu.dto.assemblers.ReceiptAssembler;
import com.ncedu.nc_edu.dto.assemblers.ReceiptStepAssembler;
import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.dto.resources.ReceiptSearchCriteria;
import com.ncedu.nc_edu.dto.resources.ReceiptStepResource;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.security.CustomUserDetails;
import com.ncedu.nc_edu.services.ReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class ReceiptController {
    private final ReceiptService receiptService;
    private final ReceiptAssembler receiptAssembler;
    private final ReceiptStepAssembler receiptStepAssembler;

    public ReceiptController(
            @Autowired ReceiptService receiptService,
            @Autowired ReceiptAssembler receiptAssembler,
            @Autowired ReceiptStepAssembler receiptStepAssembler
    ) {
        this.receiptService = receiptService;
        this.receiptAssembler = receiptAssembler;
        this.receiptStepAssembler = receiptStepAssembler;
    }

    @GetMapping("/receipts")
    public PagedModel getAll(
            Authentication auth,
            Pageable pageable
    ) {
        Page<Receipt> page = this.receiptService.findAll(pageable);

        PagedModel paged = PagedModel.wrap(page.getContent().stream().map(receiptAssembler::toModel)
                .collect(Collectors.toList()), new PagedModel.PageMetadata(
                page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()
        ));

        paged.add(linkTo(methodOn(ReceiptController.class).create(auth, null)).withRel("create"));

        return paged;
    }

    @GetMapping(value = "/receipts/{id}")
    public ReceiptResource getById(Authentication auth, @PathVariable UUID id) {
        ReceiptResource resource = this.receiptAssembler.toModel(this.receiptService.findById(id));
        return resource;
    }

    @GetMapping(value = "/receipts/{id}/steps")
    public CollectionModel<List<ReceiptStepResource>> getReceiptSteps(@PathVariable UUID id) {
        CollectionModel<List<ReceiptStepResource>> resource = new CollectionModel<>(Collections.singleton(
                this.receiptService.findById(id).getSteps().stream()
                        .map(receiptStepAssembler::toModel).collect(Collectors.toList())
        ));

        resource.add(linkTo(methodOn(ReceiptController.class).getById(null, id)).withRel("receipt"));

        return resource;
    }

    @PostMapping(value = "/receipts")
    public ReceiptResource create(Authentication auth, @RequestBody @Valid ReceiptWithStepsResource receipt) {
        User user = ((CustomUserDetails)(auth.getPrincipal())).getUser();

        receipt.getReceiptSteps().forEach(step -> {
            if (step.getDescription() == null && step.getPicture() == null) {
                throw new RequestParseException("Step must contain either picture or description");
            }
        });

        log.debug(receipt.getReceiptResource().toString());

        ReceiptResource resource = this.receiptAssembler.toModel(this.receiptService.create(receipt, user));

        log.debug(resource.toString());

        return resource;
    }

    @PutMapping(value = "/receipts/{id}")
    public ReceiptResource update(
            Authentication auth,
            @PathVariable UUID id,
            @RequestBody @Valid ReceiptWithStepsResource receipt
    ) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();


        receipt.getReceiptSteps().forEach(step -> {
            if (step.getDescription() == null && step.getPicture() == null) {
                throw new RequestParseException("Step must contain either picture or description");
            }
        });

        receipt.getReceiptResource().setId(id);

        ReceiptResource updatedResource = this.receiptAssembler.toModel(this.receiptService.update(receipt));

        return updatedResource;
    }

    @DeleteMapping(value = "/receipts/{id}")
    public ResponseEntity<Void> remove(Authentication auth, @PathVariable UUID id) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();

        this.receiptService.removeById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/receipts/search")
    public PagedModel search(
            Authentication auth,
            @RequestBody(required = false) @Valid ReceiptSearchCriteria receiptSearchCriteria,
            Pageable pageable
    ) {
        if (receiptSearchCriteria == null) {
            return this.getAll(auth, pageable);
        }

        Page<Receipt> page = receiptService.search(receiptSearchCriteria, pageable);

        PagedModel paged = PagedModel.wrap(page.getContent().stream().map(receiptAssembler::toModel)
                        .collect(Collectors.toList()), new PagedModel.PageMetadata(
                                page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()
        ));

//        resource.add(linkTo(methodOn(ReceiptController.class).search(auth, receiptSearchCriteria, pageable.next())).withRel("next"));
//        resource.add(linkTo(methodOn(ReceiptController.class).search(auth, receiptSearchCriteria, pageable.previousOrFirst())).withRel("prev"));
//        resource.add(linkTo(methodOn(ReceiptController.class).search(auth, receiptSearchCriteria, pageable.first())).withRel("first"));

        return paged;
    }
}
