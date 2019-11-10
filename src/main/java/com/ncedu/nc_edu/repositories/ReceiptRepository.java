package com.ncedu.nc_edu.repositories;

import com.ncedu.nc_edu.models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    List<Receipt> findByNameContaining(String name);
}
