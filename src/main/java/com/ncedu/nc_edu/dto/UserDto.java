package com.ncedu.nc_edu.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
    @NotNull
    private UUID id;

    private String gender;

    private String username;

    @Email
    private String email;

    @Past
    private Date birthday;

    @PositiveOrZero
    private int height;

    @PositiveOrZero
    private int weight;
}
