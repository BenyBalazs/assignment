package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.exception.NotAuthorizedOperationException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.springframework.stereotype.Service;

@Service
public interface LogInInterface {

    boolean logIn(String username, String password)
            throws UserAlreadyLoggedInException, NotAuthorizedOperationException;
}
