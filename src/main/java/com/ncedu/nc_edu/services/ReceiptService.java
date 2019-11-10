package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.models.Receipt;

import java.util.List;
import java.util.UUID;

public interface ReceiptService {
    List<Receipt> findByName(String name);
    Receipt findById(UUID id);
    List<Receipt> findAll();

}
