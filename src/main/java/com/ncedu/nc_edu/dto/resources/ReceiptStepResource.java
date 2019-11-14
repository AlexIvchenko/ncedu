package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiptStepResource extends RepresentationModel<ReceiptStepResource> {
    private UUID id;

    private String description;

    // todo replace to link for object storage
    private UUID picture;
}
