package com.epam.training.ticketservice.service.user;

import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.service.interfaces.AbstractLogIn;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.springframework.stereotype.Service;

@Service
public class LogInService extends AbstractLogIn {

    public LogInService(UserRepository userRepository, ActiveUserStore activeUserStore) {
        super(userRepository, activeUserStore);
    }

    public boolean logIn(String username, String password) throws UserAlreadyLoggedInException {

        return super.credentialCheck(username, password);
    }

}
