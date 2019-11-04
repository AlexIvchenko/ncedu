package com.ncedu.nc_edu.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_filters")
public class UserFilter extends Filter {
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
}
