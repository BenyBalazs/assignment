package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    @MockBean
    private ActiveUserStore activeUserStore;

    @BeforeEach
    public void setUp() {
        activeUserStore = Mockito.mock(ActiveUserStore.class);
        authorizationService = new AuthorizationService(activeUserStore);
    }

    @Test
    public void testUserIsLoggedInShouldThrowUserNotLoggedInExceptionWhenTheUserIsNotLoggedIn() {
        when(activeUserStore.getActiveUser()).thenReturn(null);
        assertThrows(UserNotLoggedInException.class, () -> authorizationService.userIsLoggedIn());
    }

    @Test
    public void testUserHasRolesShouldThrowUserNotLoggedInExceptionWhenTheUserIsNotLoggedIn() {
        when(activeUserStore.getActiveUser()).thenReturn(null);
        assertThrows(UserNotLoggedInException.class, () -> authorizationService.userHasRoles(User.Role.USER));
    }

    @Test
    public void testUserHasRolesShouldThrowNotAuthorizedOperationExceptionWhenTheUserDoesNotHaveOneOfTheGivenRoles() {
        when(activeUserStore.getActiveUser()).thenReturn(new User("admin", "admin", User.Role.ADMIN));
        assertThrows(NotAuthorizedOperationException.class, () -> authorizationService.userHasRoles(User.Role.USER));
    }
}
