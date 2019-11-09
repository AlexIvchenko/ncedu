package com.ncedu.nc_edu;

import com.ncedu.nc_edu.dto.UserResource;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class UserResourceValueOfEnumValidatorTest {
    @Test
    public void nullTest() {
        UserResource user = new UserResource();
        user.setGender(null);
        user.setPassword("123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void valueTest() {
        UserResource user = new UserResource();
        user.setGender("MALE");
        user.setPassword("123");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(0);

        user.setGender("asd");
        violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }
}
