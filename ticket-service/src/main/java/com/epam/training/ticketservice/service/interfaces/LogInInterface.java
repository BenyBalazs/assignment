package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.exception.NotAuthorizedLogInException;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.springframework.stereotype.Service;

@Service
public interface LogInInterface {

    boolean logIn(String username, String password) throws UserAlreadyLoggedInException, NotAuthorizedLogInException;
}
