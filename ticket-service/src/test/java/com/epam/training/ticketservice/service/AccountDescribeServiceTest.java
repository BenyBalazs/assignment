package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AccountDescribeServiceTest {

    ActiveUserStore activeUserStore;
    AccountDescribeService accountDescribeService;
    User activeUser;

    @BeforeEach
    public void setUp() {
        activeUserStore = new ActiveUserStore();
        accountDescribeService = new AccountDescribeService(activeUserStore);
        activeUser = new User("admin", "admin", User.Role.ADMIN);
    }

    @Test
    public void testShouldThrowUserNotLoggedInExceptionWhenTheUserIsNotLoggedIn() {

        assertThrows(UserNotLoggedInException.class, () -> accountDescribeService.getUser());
    }

    @Test
    public void testGetUserShouldReturnTheActiveUserWhenExecutedAndUserIsLoggedIn() throws UserNotLoggedInException {
        activeUserStore.setActiveUser(activeUser);

        assertThat(accountDescribeService.getUser(), equalTo(activeUser));
    }


}
