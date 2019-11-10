package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.repositories.ReceiptRepository;
import com.ncedu.nc_edu.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;

    public ReceiptServiceImpl(@Autowired ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public List<Receipt> findAll() {
        return this.receiptRepository.findAll();
    }

    public Receipt findById(UUID id) {
        return this.receiptRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("Receipt"));
    }

    public List<Receipt> findByName(String name) {
        return this.receiptRepository.findByNameContaining(name);
    }
}
