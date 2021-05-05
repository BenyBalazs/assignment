package com.epam.training.ticketservice.service.user;

import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.service.interfaces.SignOutInterface;
import org.springframework.stereotype.Component;

@Component
public class SignOutService implements SignOutInterface {

    ActiveUserStore activeUserStore;
    AuthorizationService authorizationService;

    public SignOutService(ActiveUserStore activeUserStore, AuthorizationService authorizationService) {
        this.activeUserStore = activeUserStore;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean singOut() throws UserNotLoggedInException {
        authorizationService.userIsLoggedIn();

        activeUserStore.setActiveUser(null);

        return true;
    }
}
