package com.ncedu.nc_edu.models;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_filters")
public class UserFilter extends Filter {
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;
}
