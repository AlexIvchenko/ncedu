package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReceiptController {
    private ReceiptService receiptService;

    public ReceiptController(@Autowired ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/receipts")
    public List<Receipt> getAll() {
        // to do
        return null;
    }
}
