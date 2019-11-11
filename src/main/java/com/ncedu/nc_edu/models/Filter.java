package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
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

    @Column(name = "enabled_from")
    private Date enabledFrom;

    @Column(name = "enabled_until")
    private Date enabledUntil;

    @OneToMany(mappedBy = "filter")
    private Set<UsersFiltersDTO> usersFiltersDTOs;

    @ManyToMany
    @JoinTable(
            name = "filters_tags",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "filters_ingredients",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

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
