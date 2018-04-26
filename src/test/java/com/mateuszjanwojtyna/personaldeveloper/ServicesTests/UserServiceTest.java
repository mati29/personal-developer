package com.mateuszjanwojtyna.personaldeveloper.ServicesTests;

import com.mateuszjanwojtyna.personaldeveloper.Entities.Audit;
import com.mateuszjanwojtyna.personaldeveloper.Entities.Role;
import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.Repositories.UserRepository;
import com.mateuszjanwojtyna.personaldeveloper.Services.RoleService;
import com.mateuszjanwojtyna.personaldeveloper.Services.impl.AuditServiceImpl;
import com.mateuszjanwojtyna.personaldeveloper.Services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserServiceImpl userServiceImpl;
    private UserRepository userRepositoryMock;
    private BCryptPasswordEncoder encoder;
    private RoleService roleServiceMock;

    @Before
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        encoder = new BCryptPasswordEncoder();
        roleServiceMock = mock(RoleService.class);
        userServiceImpl = new UserServiceImpl(userRepositoryMock, encoder, roleServiceMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testUserServiceLoadUserByUsernameWhenUsernameNotFoundThenThrowUsernameNotFoundException() {
        String username = "username";
        when(userRepositoryMock.findByUsername(username)).thenReturn(null);
        userServiceImpl.loadUserByUsername(username);
    }

    @Test
    public void testUserServiceLoadUserByUsernameWhenUsernameFoundThenReturnUserDetails() {
        String admin = "admin";
        User user = mock(User.class);
        when(userRepositoryMock.findByUsername(admin)).thenReturn(user);
        assertNotNull(userServiceImpl.loadUserByUsername(admin));
    }

    @Test
    public void testUserServiceWhenCreateThenReturnUser() {
        User user = mock(User.class);
        UserServiceImpl userServiceSpy = spy(userServiceImpl);

        doReturn(user).when(userServiceSpy).encodePassword(user);
        when(userRepositoryMock.save(user)).thenReturn(user);

        assertEquals(userServiceSpy.create(user), user);
    }

    @Test
    public void testUserServiceEncodePasswordWhenPasswordThenReturnEncryptedPassword() {
        User user = new User();
        user.setPassword("abc");
        assertTrue(userServiceImpl.encodePassword(user).getPassword().startsWith("$2"));
    }

    @Test
    public void testUserServiceEncodePasswordWhenTryEncryptSamePasswordThenReturnDifferentPassword() {
        User user = new User();
        user.setPassword("abc");
        User user2 = new User();
        user2.setPassword("abc");
        assertNotEquals(userServiceImpl.encodePassword(user).getPassword(), userServiceImpl.encodePassword(user2).getPassword());
    }

    @Test
    public void testUserServiceSetStandardRoleReturnUserWithStandardRole() {
        User user = new User();
        Role role = mock(Role.class);
        when(roleServiceMock.findByRole("ROLE_USER")).thenReturn(role);
        assertTrue(!userServiceImpl.setStandardRole(user).getRoles().isEmpty());
    }

    @Test
    public void testUserServiceDeleteUserWhenWrongIdThenReturnNull() {
        int wrongId = -1;
        when(userRepositoryMock.findById(wrongId)).thenReturn(null);
        assertNull(userServiceImpl.delete(wrongId));
    }

    @Test
    public void testUserServiceDeleteUserWhenCorrectIdThenReturnUser() {
        User user = mock(User.class);
        int correctId = 0;
        when(userRepositoryMock.findById(correctId)).thenReturn(user);
        assertEquals(userServiceImpl.delete(correctId), user);
    }

    @Test
    public void testUserServiceUsernameUniqueWhenUsernameNotExistThenReturnTrue() {
        String username = "username";
        when(userRepositoryMock.existsByUsername(username)).thenReturn(false);
        assertTrue(userServiceImpl.usernameUnique(username));
    }

    @Test
    public void testUserServiceUsernameUniqueWhenUsernameExistThenReturnFalse() {
        String username = "admin";
        when(userRepositoryMock.existsByUsername(username)).thenReturn(true);
        assertFalse(userServiceImpl.usernameUnique(username));
    }



}
