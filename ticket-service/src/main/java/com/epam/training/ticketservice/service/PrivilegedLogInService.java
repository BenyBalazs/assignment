package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.utils.ActiveUserStore;
import com.epam.training.ticketservice.data.entity.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import com.epam.training.ticketservice.service.interfaces.AbstractLogIn;
import org.springframework.stereotype.Service;

@Service
public class PrivilegedLogInService extends AbstractLogIn {

    public PrivilegedLogInService(UserRepository userRepository, ActiveUserStore activeUserStore) {
        super(userRepository, activeUserStore);
    }

    @Override
    public boolean logIn(String username, String password) throws UserAlreadyLoggedInException,
            NotAuthorizedOperationException {

        boolean validCredentials = super.credentialCheck(username,password);

        User user = super.getUser(username);

        if (validCredentials && User.Role.USER.equals(user.getRole())) {
            throw new NotAuthorizedOperationException();
        }

        return validCredentials;
    }
}
