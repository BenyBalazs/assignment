package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.dao.User;

public interface RegistrationInterface {

    boolean register(User user);
}
