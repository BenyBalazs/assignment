package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.service.LogInService;
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

@SpringBootTest(classes = {LogInService.class, ActiveUserStore.class})
public class LogInCommandTest {

    private LogInCommand logInCommand;
    @Autowired
    private LogInService logInService;
    @Autowired
    private ActiveUserStore activeUserStore;
    @MockBean
    private UserRepository userRepository;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        activeUserStore.setActiveUser(null);
        when(this.userRepository.findById(Mockito.any(String.class))).
                then( x -> users.stream().filter(y -> x.getArgument(0).equals(y.getUsername())).findFirst());
    }

    @Test
    public void testCommandShouldReturnEmptyStringWhenLogInWasSuccessful() {
        users.add(new User("admin", "admin", User.Role.ADMIN));
        logInCommand = new LogInCommand(logInService, "admin", "admin");

        assertThat(logInCommand.execute(), equalTo(""));
    }

    @Test
    public void testCommandShouldReturnLoginFailureStringWhenInvalidCredentialsAreGiven() {
        users.add(new User("admin", "admin", User.Role.ADMIN));
        logInCommand = new LogInCommand(logInService, "admin2", "admin");

        assertThat(logInCommand.execute(), equalTo("Login failed due to incorrect credentials."));
    }

    @Test
    public void testCommandShouldReturnAlreadyLoggedInStringWhenTryingToLogInWhenAlreadyLoggedIn() {
        users.add(new User("admin", "admin", User.Role.ADMIN));
        logInCommand = new LogInCommand(logInService, "admin", "admin");
        logInCommand.execute();

        assertThat(logInCommand.execute(), equalTo("Already Logged in."));
    }
}
