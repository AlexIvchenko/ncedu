package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.dto.assemblers.UserInfoAssembler;
import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.dto.resources.ReceiptStepResource;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.Receipt;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class ReceiptWithStepsDTO {
    private ReceiptResource receiptResource;
    private List<ReceiptStepResource> receiptSteps;

    public ReceiptWithStepsDTO(ReceiptResource receiptResource, List<ReceiptStepResource> receiptSteps) {
        this.receiptResource = receiptResource;
        this.receiptSteps = receiptSteps;
    }
}
