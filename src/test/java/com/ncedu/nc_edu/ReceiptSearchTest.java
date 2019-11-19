package com.ncedu.nc_edu;

import com.ncedu.nc_edu.dto.resources.ReceiptSearchCriteria;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.services.ReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Slf4j
@SpringBootTest
public class ReceiptSearchTest {
    @Test
    public void searchTest(@Autowired ReceiptService receiptServices) {
        ReceiptSearchCriteria criteria = new ReceiptSearchCriteria();

        criteria.setCaloriesMin(100);
        criteria.setIncludeTags(Set.of("Tag1"));
        criteria.setExcludeTags(Set.of("Tag3"));
        criteria.setFatsMin(40f);

        Page<Receipt> receipts = receiptServices.search(criteria, Pageable.unpaged());

        receipts.stream().forEach(receipt -> log.info(receipt.getName()));

        assert(true);
    }
}
