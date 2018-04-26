package com.mateuszjanwojtyna.personaldeveloper.ControllersTests;

import com.mateuszjanwojtyna.personaldeveloper.Controllers.UserController;
import com.mateuszjanwojtyna.personaldeveloper.DTO.UserData;
import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.Services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;
    private UserService userServiceMock;

    @Before
    public void setUp() {
        userServiceMock = mock(UserService.class);
        userController = new UserController(userServiceMock);
    }

    @Test
    public void testUserControllerfindAllReturnAllUsers() {
        List<User> userList = mock(List.class);
        when(userServiceMock.findAll()).thenReturn(userList);
        assertNotNull(userController.findAll());
    }

    @Test
    public void testUserControllerDeleteUserWhenWrongIdThenReturnNull() {
        int wrongId = -1;
        when(userServiceMock.delete(wrongId)).thenReturn(null);
        assertNull(userController.delete(wrongId));
    }

    @Test
    public void testUserControllerDeleteUserWhenCorrectIdThenReturnNotNull() {
        int correctId = 0;
        User user = mock(User.class);
        when(userServiceMock.delete(correctId)).thenReturn(user);
        assertNotNull(userController.delete(correctId));
    }

    @Test
    public void testUserControllerFindOneUserWhenWrongIdThenReturnNull() {
        int wrongId = -1;
        when(userServiceMock.findById(wrongId)).thenReturn(null);
        assertNull(userController.findOne(wrongId));
    }

    @Test
    public void testUserControllerFindOneUserWhenCorrectIdThenReturnNotNull() {
        int correctId = 0;
        User user = mock(User.class);
        when(userServiceMock.findById(correctId)).thenReturn(user);
        assertNotNull(userController.findOne(correctId));
    }

    @Test
    public void testUserControllerCreateUserWhenNullUserDataThenReturnNull() {
        UserData userData = null;
        assertNull(userController.create(userData));
    }

    @Test
    public void testUserControllerCreateUserWhenNotNullUserDataThenReturnNotNull() {
        UserData userData = new UserData();
        User user = new User();
        when(userServiceMock.create(any(User.class))).thenReturn(user);
        assertNotNull(userController.create(userData));
    }

}
