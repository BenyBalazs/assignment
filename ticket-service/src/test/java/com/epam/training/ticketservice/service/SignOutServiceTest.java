package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.service.user.SignOutService;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = {SignOutService.class, ActiveUserStore.class})
public class SignOutServiceTest {

    @Autowired
    private SignOutService signOutService;
    @Autowired
    private ActiveUserStore activeUserStore;

    @BeforeEach
    public void setUp() {
        activeUserStore.setActiveUser(new User("admin", "admin", User.Role.ADMIN));
    }

    @Test
    public void testShouldReturnTrueWhenSignOutIsExecuted() {

        assertThat(signOutService.singOut(), equalTo(true));
    }

    @Test
    public void testActiveUserShouldBeNullAfterTheSignOutCommandIsExecuted() {
        signOutService.singOut();

        assertThat(activeUserStore.getActiveUser(), is(nullValue()));
    }
}
