package com.epam.training.ticketservice.commands.account;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.presentation.cli.handler.UserCommandHandler;
import com.epam.training.ticketservice.service.PrivilegedLogInService;
import com.epam.training.ticketservice.service.user.AuthorizationService;
import com.epam.training.ticketservice.service.user.LogInService;
import com.epam.training.ticketservice.service.user.SignOutService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {LogInService.class, ActiveUserStore.class, PrivilegedLogInService.class, SignOutService.class, AuthorizationService.class})
public class LogInCommandTest {

    private UserCommandHandler logInCommand;
    @Autowired
    private LogInService logInService;
    @Autowired
    private PrivilegedLogInService privilegedLogInService;
    @Autowired
    private ActiveUserStore activeUserStore;
    @MockBean
    private SignOutService signOutService;
    @MockBean
    private UserRepository userRepository;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        activeUserStore.setActiveUser(null);
        when(this.userRepository.findById(Mockito.any(String.class))).
                then( x -> users.stream().filter(y -> x.getArgument(0).equals(y.getUsername())).findFirst());
        logInCommand = new UserCommandHandler(logInService, privilegedLogInService, signOutService);
    }

    @Test
    public void testCommandShouldReturnEmptyStringWhenLogInWasSuccessful() {
        users.add(new User("admin", "admin", User.Role.ADMIN));


        assertThat(logInCommand.singIn("admin", "admin"), equalTo(""));
    }

    @Test
    public void testCommandShouldReturnLoginFailureStringWhenInvalidCredentialsAreGiven() {
        users.add(new User("admin", "admin", User.Role.ADMIN));

        assertThat(logInCommand.singIn("admin2", "admin"), equalTo("Login failed due to incorrect credentials."));
    }

    @Test
    public void testCommandShouldReturnAlreadyLoggedInStringWhenTryingToLogInWhenAlreadyLoggedIn() {
        users.add(new User("admin", "admin", User.Role.ADMIN));
        logInCommand.singIn("admin", "admin");

        assertThat(logInCommand.singIn("admin", "admin"), equalTo("You are already logged in."));
    }
}
