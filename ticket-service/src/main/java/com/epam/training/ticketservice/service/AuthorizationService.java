package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {

    ActiveUserStore activeUserStore;

    public AuthorizationService(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    public void userIsAdminAndLoggedIn() throws UserNotLoggedInException, NotAuthorizedOperationException {

        if (activeUserStore.getActiveUser() == null) {
            throw new UserNotLoggedInException();
        }

        if (User.Role.USER.equals(activeUserStore.getActiveUser().getRole())) {
            throw new NotAuthorizedOperationException();
        }
    }
}
