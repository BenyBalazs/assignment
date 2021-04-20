package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.exception.UserNotLoggedInException;
import org.springframework.stereotype.Component;

@Component
public interface AccountDescribeInterface {

    User getUser() throws UserNotLoggedInException;
}
