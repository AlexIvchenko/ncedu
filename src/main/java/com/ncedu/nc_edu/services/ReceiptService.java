package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.ReceiptWithStepsResource;
import com.ncedu.nc_edu.dto.resources.ReceiptSearchCriteria;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ReceiptService {

    List<Receipt> findByName(String name);
    Receipt findById(UUID id);
    Page<Receipt> findAll(Pageable pageable);
    List<Receipt> findAllOwn(User user);
    void removeById(UUID id);
    Receipt update(ReceiptWithStepsResource dto);
    Receipt create(ReceiptWithStepsResource dto, User owner);
    Receipt cloneRec(UUID id, User user);
    Page<Receipt> search(ReceiptSearchCriteria receiptSearchCriteria, Pageable pageable);

}
