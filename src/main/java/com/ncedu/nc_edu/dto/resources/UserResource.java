package com.ncedu.nc_edu.dto.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncedu.nc_edu.dto.assemblers.UserAssembler;
import com.ncedu.nc_edu.dto.validators.ValueOfEnum;
import com.ncedu.nc_edu.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResource extends RepresentationModel<UserResource> {
    private UUID id;

    @ValueOfEnum(value = User.Gender.class, message = "Gender must be any of MALE|FEMALE|UNKNOWN")
    private String gender;

    @Size(min = 3, max = 64, message = "Username must be more that 3  and less than 64 characters")
    private String username;

    @Email(message = "Email must be a valid email")
    private String email;

    /**
     * Password shouldn't be returned to client. Do NOT set it in {@link UserAssembler}.
     */
    private String password;

    @Past(message = "Birthday must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date birthday;

    @PositiveOrZero(message = "Height must be positive or 0 for deletion")
    private Integer height;

    @PositiveOrZero(message = "Weight must be positive or 0 for deletion")
    private Integer weight;
}
