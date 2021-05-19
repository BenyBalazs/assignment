package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.presentation.cli.handler.UserCommandHandler;
import com.epam.training.ticketservice.service.PrivilegedLogInService;
import com.epam.training.ticketservice.service.user.LogInService;
import com.epam.training.ticketservice.service.user.SignOutService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {LogInService.class, ActiveUserStore.class})
public class PrivilegedLogInCommandTest {

    private UserCommandHandler logInCommand;
    @Autowired
    private LogInService logInService;
    @MockBean
    private SignOutService signOutService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    PrivilegedLogInService privilegedLogInService;

    @BeforeEach
    public void setUp() {
        privilegedLogInService = Mockito.mock(PrivilegedLogInService.class);
        logInCommand = new UserCommandHandler(logInService, privilegedLogInService, signOutService);
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnEmptyStringWhenExecutionWasSuccessful() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenReturn(true);
        assertThat(logInCommand.singInPrivileged("asd", "asd"), nullValue());
    }

    @SneakyThrows
    @Test
    public void testExecuteLoginFailedWhenIncorrectCredentialsAreGiven() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenReturn(false);
        assertThat(logInCommand.singInPrivileged("asd", "asd"), equalTo("Login failed due to incorrect credentials"));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAlreadyLoggedInUserAlreadyLoggedInExceptionIsThrown() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenThrow(new UserAlreadyLoggedInException());
        assertThat(logInCommand.singInPrivileged("asd", "asd"), equalTo("You are already logged in"));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAdminCommandWhenNotAuthorizedOperationExceptionIsThrown() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenThrow(new NotAuthorizedOperationException());
        assertThat(logInCommand.singInPrivileged("asd", "asd"), equalTo("This is and admin command"));
    }

}
