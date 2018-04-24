package com.mateuszjanwojtyna.personaldeveloper.ValidatorsTests;

import com.mateuszjanwojtyna.personaldeveloper.DTO.UserData;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.UserRepository;
import com.mateuszjanwojtyna.personaldeveloper.Validators.UniqueUsernameValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.*;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDataValidationTest implements ConstraintValidatorFactory {

    @Mock
    private UserRepository userRepositoryMock;

    private static Validator validator;

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> aClass) {
        if (aClass == UniqueUsernameValidator.class) {
            return (T) new UniqueUsernameValidator(userRepositoryMock);
        }

        try{
            return aClass.newInstance();
        }catch(InstantiationException | IllegalAccessException exception){}
        throw new IllegalArgumentException("expecting SomeValidationValidator!");
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> constraintValidator) {

    }

    @Before
    public void setUp() {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        config.constraintValidatorFactory(this);

        ValidatorFactory factory = config.buildValidatorFactory();

        validator = factory.getValidator();
    }

    @Test
    public void testUserDataWhenCorrectFirstNameThenPassValidation() {
        UserData userData = new UserData();
        userData.setFirstName("aaa");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenInccorrectFirstNameThenRefuseValidation() {
        UserData userData = new UserData();
        userData.setFirstName("a");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenCorrectLastNameThenPassValidation() {
        UserData userData = new UserData();
        userData.setLastName("aaa");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenInccorrectLastNameThenRefuseValidation() {
        UserData userData = new UserData();
        userData.setLastName("a");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenCorrectPasswordThenPassValidation() {
        UserData userData = new UserData();
        userData.setPassword("aaa");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenInccorrectPasswordThenRefuseValidation() {
        UserData userData = new UserData();
        userData.setPassword("a");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenCorrectEmailThenPassValidation() {
        UserData userData = new UserData();
        userData.setEmail("a@o.p");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenInccorrectEmailThenRefuseValidation() {
        UserData userData = new UserData();
        userData.setEmail("a@o..pl");
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenCorrectUsernameThenPassValidation() {
        UserData userData = new UserData();
        userData.setUsername("user");
        when(userRepositoryMock.existsByUsername("user")).thenReturn(false);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserDataWhenInccorrectUsernameThenRefuseValidation() {
        UserData userData = new UserData();
        userData.setUsername("admin");
        when(userRepositoryMock.existsByUsername("admin")).thenReturn(true);
        Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
        assertFalse(violations.isEmpty());
    }


}
