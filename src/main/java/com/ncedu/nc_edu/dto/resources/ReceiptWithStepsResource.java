package com.ncedu.nc_edu.dto.resources;

import lombok.Data;

import java.util.List;

@Data
public class ReceiptWithStepsResource {
    private ReceiptResource info;
    private List<ReceiptStepResource> steps;

    public ReceiptWithStepsResource(ReceiptResource info, List<ReceiptStepResource> steps) {
        this.info = info;
        this.steps = steps;
    }
}
