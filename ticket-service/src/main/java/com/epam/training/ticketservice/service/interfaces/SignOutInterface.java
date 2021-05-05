package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Service;

@Service
public interface SignOutInterface {

    boolean singOut() throws UserNotLoggedInException;
}
