package com.ncedu.nc_edu.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users_filters", schema = "public")
public class UsersFiltersDTO {
    @EmbeddedId
    private UsersFiltersId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filter_id")
    private Filter filter;

    @Column(name = "enabled_until")
    private Date enabledUntil;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersFiltersDTO that = (UsersFiltersDTO) o;
        return user.equals(that.user) &&
                filter.equals(that.filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, filter);
    }
}