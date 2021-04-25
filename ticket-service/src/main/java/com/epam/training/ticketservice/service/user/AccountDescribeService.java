package com.epam.training.ticketservice.service.user;

import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AccountDescribeInterface;
import org.springframework.stereotype.Service;

@Service
public class AccountDescribeService implements AccountDescribeInterface {

    ActiveUserStore activeUserStore;
    UserRepository userRepository;

    public AccountDescribeService(ActiveUserStore activeUserStore, UserRepository userRepository) {
        this.activeUserStore = activeUserStore;
        this.userRepository = userRepository;
    }

    @Override
    public User getUser() throws UserNotLoggedInException {

        if (activeUserStore.getActiveUser() == null) {
            throw new UserNotLoggedInException();
        }

        return userRepository.findById(activeUserStore.getActiveUser().getUsername()).get();
    }
}
