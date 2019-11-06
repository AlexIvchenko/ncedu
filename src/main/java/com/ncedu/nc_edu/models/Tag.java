package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Tag {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "tags_receipts",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "receipt_id")
    )
    private Set<Receipt> receipts;
}
