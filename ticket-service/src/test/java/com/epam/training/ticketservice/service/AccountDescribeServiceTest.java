package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.user.AccountDescribeService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class AccountDescribeServiceTest {

    ActiveUserStore activeUserStore;
    AccountDescribeService accountDescribeService;
    User activeUser;
    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        activeUserStore = new ActiveUserStore();
        accountDescribeService = new AccountDescribeService(activeUserStore, userRepository);
        activeUser = new User("admin", "admin", User.Role.ADMIN);
    }

    @Test
    public void testShouldThrowUserNotLoggedInExceptionWhenTheUserIsNotLoggedIn() {

        assertThrows(UserNotLoggedInException.class, () -> accountDescribeService.getUser());
    }

    @Test
    public void testGetUserShouldReturnTheActiveUserWhenExecutedAndUserIsLoggedIn() throws UserNotLoggedInException {
        activeUserStore.setActiveUser(activeUser);
        when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(activeUser));
        assertThat(accountDescribeService.getUser(), equalTo(activeUser));
    }


}
