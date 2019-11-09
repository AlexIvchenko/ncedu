package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.repositories.ReceiptRepository;
import com.ncedu.nc_edu.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;

    public ReceiptServiceImpl(@Autowired ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }


}
