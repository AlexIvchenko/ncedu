package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Data
public class UsersFiltersId implements Serializable {
    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "filter_id")
    private UUID filter_id;

    public UsersFiltersId() {
    }

    public UsersFiltersId(UUID user_id, UUID filter_id) {
        this.user_id = user_id;
        this.filter_id = filter_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersFiltersId that = (UsersFiltersId) o;
        return user_id.equals(that.user_id) &&
                filter_id.equals(that.filter_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, filter_id);
    }
}
