package com.ncedu.nc_edu.repositories;

import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    List<Receipt> findByNameContaining(String name);
    List<Receipt> findByOwner(User user);

/*
    @Query("SELECT r FROM Receipt r WHERE name " +
            "LIKE %:namePattern% AND (:includedTags) IN (r.tags) AND (:excludedTags) NOT IN (r.tags)")
    Page<Receipt> findWithFilter(Pageable pageable,
                                 @Param("namePattern") String name,
                                 @Param("includedTags") Set<Tag> includedTags,
                                 @Param("excludedTags") Set<Tag> excludedTags
    );
*/

    Page<Receipt> findAllByNameContainingAndTagsIsInAndTagsIsNotIn(Pageable pageable,
                                 @Param("namePattern") String name,
                                 @Param("includedTags") Set<Tag> includedTags,
                                 @Param("excludedTags") Set<Tag> excludedTags
    );
}
