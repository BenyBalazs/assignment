package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RegistrationService.class)
public class RegisterServiceTest {

    @Autowired
    private RegistrationService registrationService;
    @MockBean
    private UserRepository userRepository;
    private List<User> users;

    @BeforeEach
    public void setUp(){
        users = new ArrayList<>();
        when(this.userRepository.findById(Mockito.any(String.class))).
                then( x -> users.stream().filter(y -> x.getArgument(0).equals(y.getUsername())).findFirst());
    }

    @Test
    public void testRegisterUserShouldReturnTrueWhenRegisteringRandomUser(){
        assertThat(registrationService.register(new User("admin","admin", User.Role.ADMIN)), equalTo(true));
    }

    @Test
    public void testRegisterUserShouldReturnFalseWhenTryingToRegisterNewUserButTheUsernameAlreadyExists() {
        users.add(new User("admin","admin", User.Role.ADMIN));

        assertThat(registrationService.register(new User("admin","admin", User.Role.ADMIN)), equalTo(false));
    }
}
