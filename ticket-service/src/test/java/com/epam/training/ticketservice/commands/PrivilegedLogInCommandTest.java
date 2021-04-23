package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.commands.account.PrivilegedLogInCommand;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.service.PrivilegedLogInService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

public class PrivilegedLogInCommandTest {

    PrivilegedLogInCommand privilegedLogInCommand;
    @MockBean
    PrivilegedLogInService privilegedLogInService = Mockito.mock(PrivilegedLogInService.class);

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnEmptyStringWhenExecutionWasSuccessful() {
        privilegedLogInCommand = new PrivilegedLogInCommand(privilegedLogInService, "asd", "asd");
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenReturn(true);
        assertThat(privilegedLogInCommand.execute(), equalTo(""));
    }

    @SneakyThrows
    @Test
    public void testExecuteLoginFailedWhenIncorrectCredentialsAreGiven() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenReturn(false);
        privilegedLogInCommand = new PrivilegedLogInCommand(privilegedLogInService, "asd", "asd");
        assertThat(privilegedLogInCommand.execute(), equalTo("Login failed due to incorrect credentials."));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAlreadyLoggedInUserAlreadyLoggedInExceptionIsThrown() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenThrow(new UserAlreadyLoggedInException());
        privilegedLogInCommand = new PrivilegedLogInCommand(privilegedLogInService, "asd", "asd");
        assertThat(privilegedLogInCommand.execute(), equalTo("You are already logged in."));
    }

    @SneakyThrows
    @Test
    public void testExecuteShouldReturnAdminCommandWhenNotAuthorizedOperationExceptionIsThrown() {
        when(privilegedLogInService.logIn(Mockito.any(String.class), Mockito.anyString())).thenThrow(new NotAuthorizedOperationException());
        privilegedLogInCommand = new PrivilegedLogInCommand(privilegedLogInService, "asd", "asd");
        assertThat(privilegedLogInCommand.execute(), equalTo("This is and admin command"));
    }

}
