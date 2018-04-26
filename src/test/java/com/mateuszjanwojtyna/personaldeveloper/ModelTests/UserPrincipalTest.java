package com.mateuszjanwojtyna.personaldeveloper.ModelTests;

import com.mateuszjanwojtyna.personaldeveloper.Entities.User;
import com.mateuszjanwojtyna.personaldeveloper.Models.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserPrincipalTest {

    @Test
    public void testUserPrincipalGetAuthoritiesWhenUserMockedThenReturnAuthoritiesNotNull() {
        User user = mock(User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertNotNull(userPrincipal.getAuthorities());
    }

    @Test
    public void testUserPrincipalGetAuthoritiesWhenUserEmptyThenReturnAuthoritiesNull() {
        User user = new User();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        assertNull(userPrincipal.getAuthorities());
    }

}
