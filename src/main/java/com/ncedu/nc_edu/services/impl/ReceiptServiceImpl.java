package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.ReceiptResource;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.exceptions.RequestParseException;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.ReceiptStep;
import com.ncedu.nc_edu.models.Tag;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.repositories.ReceiptRepository;
import com.ncedu.nc_edu.services.ReceiptService;
import com.ncedu.nc_edu.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final TagService tagService;

    public ReceiptServiceImpl(
            @Autowired ReceiptRepository receiptRepository,
            @Autowired TagService tagService
    ) {
        this.receiptRepository = receiptRepository;
        this.tagService = tagService;
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

    @Override
    public List<Receipt> findAllOwn(User user) {
        return this.receiptRepository.findByOwner(user);
    }

    @Override
    public void removeById(UUID id) {
        this.receiptRepository.deleteById(id);
    }

    @Override
    public Receipt update(ReceiptResource resource) {
        Receipt oldReceipt = this.receiptRepository.findById(resource.getId())
                .orElseThrow(() -> new EntityDoesNotExistsException("Receipt"));

        if (resource.getName() != null) {
            oldReceipt.setName(resource.getName());
        }

        if (resource.getCalories() != null) {
            oldReceipt.setCalories(resource.getCalories() == 0 ? null : resource.getCalories());
        }

        if (resource.getFats() != null) {
            oldReceipt.setFats(resource.getFats() == 0 ? null : resource.getFats());
        }

        if (resource.getProteins() != null) {
            oldReceipt.setProteins(resource.getProteins() == 0 ? null : resource.getProteins());
        }

        if (resource.getCarbohydrates() != null) {
            oldReceipt.setCarbohydrates(resource.getCarbohydrates() == 0 ? null : resource.getCarbohydrates());
        }

        if (resource.getTags() != null) {
            oldReceipt.setTags(resource.getTags().stream()
                    .map(tagResource -> tagService.findById(tagResource.getId())).collect(Collectors.toSet()));
        }

        if (resource.getSteps() != null) {
            List<ReceiptStep> steps = oldReceipt.getSteps();
            Map<UUID, ReceiptStep> stepMap = new LinkedHashMap<>();
            for (ReceiptStep step : steps) {
                stepMap.put(step.getId(), step);
            }

            oldReceipt.setSteps(resource.getSteps().stream().map(stepResource -> {
                ReceiptStep step;
                if (stepResource.getId() != null) {
                    if (stepMap.containsKey(stepResource.getId())) {
                        step = stepMap.get(stepResource.getId());
                    } else {
                        throw new RequestParseException("Invalid step ID");
                    }
                } else {
                    step = new ReceiptStep();
                    step.setId(UUID.randomUUID());
                    step.setReceipt(oldReceipt);
                }

                if (stepResource.getDescription() != null) {
                    step.setDescription(stepResource.getDescription());
                }

                if (stepResource.getPicture() != null) {
                    step.setPicture(stepResource.getPicture());
                }

                return step;
            }).collect(Collectors.toList()));
        }

        return this.receiptRepository.save(oldReceipt);
    }

    @Override
    public Receipt create(ReceiptResource resource, User owner) {
        Receipt receipt = new Receipt();

        receipt.setId(UUID.randomUUID());
        receipt.setName(resource.getName());
        receipt.setCarbohydrates(resource.getCarbohydrates());
        receipt.setProteins(resource.getProteins());
        receipt.setCalories(resource.getCalories());
        receipt.setFats(resource.getFats());
        receipt.setRating(0f);
        receipt.setOwner(owner);

        if (resource.getTags() != null) {
            receipt.setTags(resource.getTags().stream()
                    .map(tagResource -> tagService.findById(tagResource.getId())).collect(Collectors.toSet()));
        }

        if (resource.getSteps() == null) {
            throw new RequestParseException("Receipt must contain at least 1 step");
        }

        receipt.setSteps(resource.getSteps().stream().map(receiptStepResource -> {
            ReceiptStep step = new ReceiptStep();
            step.setId(UUID.randomUUID());
            step.setDescription(receiptStepResource.getDescription());
            step.setPicture(receiptStepResource.getPicture());
            step.setReceipt(receipt);
            return step;
        }).collect(Collectors.toList()));

        log.info(receipt.toString());

        return this.receiptRepository.save(receipt);
    }

    @Override
    public Page<Receipt> search(
            Pageable pageable,
            String name,
            Set<UUID> includeTags,
            Set<UUID> excludeTags
    ) {
        Set<Tag> tagsToInclude = includeTags.stream().map(tagService::findById).collect(Collectors.toSet());
        Set<Tag> tagsToExclude = excludeTags.stream().map(tagService::findById).collect(Collectors.toSet());

        return receiptRepository.findAllByNameContainingAndTagsIsInAndTagsIsNotIn(pageable, name, tagsToInclude, tagsToExclude);
    }
}
