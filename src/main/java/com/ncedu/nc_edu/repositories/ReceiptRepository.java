package com.ncedu.nc_edu.repositories;

import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    List<Receipt> findByNameContaining(String name);
    List<Receipt> findByOwner(User user);

    Page<Receipt> findAll(Pageable pageable);
    Page<Receipt> findAll(Specification<Receipt> specification, Pageable pageable);
}
