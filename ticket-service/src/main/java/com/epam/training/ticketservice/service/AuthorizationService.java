package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthorizationService {

    ActiveUserStore activeUserStore;

    public AuthorizationService(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    public void userIsLoggedIn() throws UserNotLoggedInException {
        if (activeUserStore.getActiveUser() == null) {
            throw new UserNotLoggedInException();
        }
    }

    public void userHasRoles(User.Role... roles) throws UserNotLoggedInException, NotAuthorizedOperationException {
        userHasRole(Arrays.asList(roles));
    }

    private void userHasRole(List<User.Role> acceptedRoles) throws NotAuthorizedOperationException,
            UserNotLoggedInException {

        userIsLoggedIn();

        if (!acceptedRoles.contains(activeUserStore.getActiveUser().getRole())) {
            throw new NotAuthorizedOperationException();
        }
    }
}
