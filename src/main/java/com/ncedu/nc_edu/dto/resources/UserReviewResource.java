package com.ncedu.nc_edu.dto.resources;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.UUID;

@Data
public class UserReviewResource extends RepresentationModel<UserReviewResource> {
    private UUID id;

    @PastOrPresent(message = "Creation date must be past or present")
    private Date created_on;

    @Positive(message = "Rating must be positive")
    private Float rating;

    private String review;
}
