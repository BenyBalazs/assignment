package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;
import org.springframework.stereotype.Service;

@Service
public class AccountDescribeService implements AccountDescribeInterface {

    ActiveUserStore activeUserStore;

    public AccountDescribeService(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    @Override
    public User getUser() throws UserNotLoggedInException {

        if (activeUserStore.getActiveUser() == null) {
            throw new UserNotLoggedInException();
        }

        return activeUserStore.getActiveUser();
    }
}
