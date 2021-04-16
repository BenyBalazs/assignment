package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.service.interfaces.RegistrationInterface;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService implements RegistrationInterface {

    UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user) {

        if(userRepository.findById(user.getUsername()).isPresent())
            return false;

        userRepository.save(user);
        return true;
    }
}
