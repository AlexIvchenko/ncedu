package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.dto.validators.ValueOfEnum;
import com.ncedu.nc_edu.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResource extends RepresentationModel<UserResource> {
    private UUID id;

    @ValueOfEnum(User.Gender.class)
    private String gender;

    @Size(min = 3, max = 64)
    private String username;

    @Email
    private String email;

    /**
     * Password shouldn't be returned to client. Do NOT set it in {@link UserAssembler}.
     */
    @NotBlank
    private String password;

    @Past
    private Date birthday;

    @PositiveOrZero
    private Integer height;

    @PositiveOrZero
    private Integer weight;
}
