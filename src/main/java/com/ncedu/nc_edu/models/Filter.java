package com.ncedu.nc_edu.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
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
    private Set<UsersFilters> usersFilters;

    @ManyToMany
    @JoinTable(
            name = "filters_tags",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return id.equals(filter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Filter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enabledFrom=" + enabledFrom +
                ", enabledUntil=" + enabledUntil +
                '}';
    }
}
