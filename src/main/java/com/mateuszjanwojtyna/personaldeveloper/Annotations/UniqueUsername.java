package com.mateuszjanwojtyna.personaldeveloper.Annotations;

import com.mateuszjanwojtyna.personaldeveloper.Validators.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
