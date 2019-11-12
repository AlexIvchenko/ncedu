package com.ncedu.nc_edu;

import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.repositories.ReceiptRepository;
import com.ncedu.nc_edu.services.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
public class ReceiptSearchRepositoryTest {
    private final ReceiptRepository receiptRepository;
    private final TagService tagService;

    public ReceiptSearchRepositoryTest(@Autowired ReceiptRepository receiptRepository, @Autowired TagService tagService) {
        this.receiptRepository = receiptRepository;
        this.tagService = tagService;
    }

    @Test
    public void searchTest() {
        String name = "pt";

        Tag tag1 = tagService.findById(UUID.fromString("5e975143-61ac-4fc2-8bd2-b10aafe80c68"));
        Tag tag2 = tagService.findById(UUID.fromString("9caab7cb-6256-4c73-a572-a0329288fe8b"));
        Set<Tag> incl = new HashSet<>();
        incl.add(tag1);
        Set<Tag> excl = new HashSet<>();
        excl.add(tag2);

        Page<Receipt> page = receiptRepository.findAllByNameContainingAndTagsIsInAndTagsIsNotIn(
                Pageable.unpaged(), name, incl, excl);

        System.out.println(page);
    }
}
