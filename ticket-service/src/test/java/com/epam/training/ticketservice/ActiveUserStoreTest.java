package com.epam.training.ticketservice;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActiveUserStoreTest {

    ActiveUserStore activeUserStore;

    @BeforeEach
    public void setUp() {
        activeUserStore = new ActiveUserStore();
    }

    @Test
    public void testGetActiveUserShouldReturnNullAfterTheUserStoreHasBeenCreated() {

        assertThat(activeUserStore.getActiveUser(), is(nullValue()));
    }

    @Test
    public void testSetActiveUserShouldSetTheActiveUser() {
        User userToSet = new User("admin", "admin", User.Role.ADMIN);
        activeUserStore.setActiveUser(userToSet);
        assertThat(activeUserStore.getActiveUser(), equalTo(userToSet));

    }
}
