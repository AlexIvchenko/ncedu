package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "receipt_steps", schema = "public")
public class ReceiptStep {
    @Id
    private UUID id;

    private String description;

    @ManyToOne
    private Receipt receipt;
}
