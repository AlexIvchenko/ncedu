package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReceiptService {
    List<Receipt> findByName(String name);
    Receipt findById(UUID id);
    List<Receipt> findAll();
    List<Receipt> findAllOwn(User user);
    void removeById(UUID id);
    Receipt update(ReceiptResource resource);
    Receipt create(ReceiptResource resource, User owner);
    Page<Receipt> search(Pageable pageable, String name, Set<UUID> includeTags, Set<UUID> excludeTags);
}
