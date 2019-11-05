package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "global_filters", schema = "public")
@Data
public class Filter {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    private String name;

    private Date enabledFrom;
    private Date enabledUntil;

    public boolean isEnabledNow() {
        Date now = new Date();
        if (enabledUntil == null) {
            return enabledFrom.compareTo(now) <= 0;
        }

        if (enabledFrom == null) {
            return enabledUntil.compareTo(now) > 0;
        }

        return enabledFrom.compareTo(now) <= 0 && enabledUntil.compareTo(now) > 0;
    }
}
