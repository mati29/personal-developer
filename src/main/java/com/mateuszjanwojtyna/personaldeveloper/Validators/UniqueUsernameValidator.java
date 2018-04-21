package com.mateuszjanwojtyna.personaldeveloper.Validators;

import com.mateuszjanwojtyna.personaldeveloper.Annotations.UniqueUsername;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initialize(UniqueUsername constraint) {
    }

    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(username);
    }

}
