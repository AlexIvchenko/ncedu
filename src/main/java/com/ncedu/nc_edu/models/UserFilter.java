package com.ncedu.nc_edu.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_filters")
public class UserFilter extends Filter {
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;
}
