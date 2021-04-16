package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.service.interfaces.LogInInterface;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class LogInService implements LogInInterface{

    UserRepository userRepository;
    ActiveUserStore activeUserStore;

    public LogInService(UserRepository userRepository, ActiveUserStore activeUserStore) {
        this.userRepository = userRepository;
        this.activeUserStore = activeUserStore;
    }

    public boolean logIn(String username, String password) throws UserAlreadyLoggedInException {

        Optional<User> user = userRepository.findById(username);

        if(user.isEmpty())
            return false;

        if(activeUserStore.getActiveUser() != null)
            throw new UserAlreadyLoggedInException();

        boolean validCredentials = user.get().getPassword().equals(password);

        if(validCredentials)
            setActiveUser(user.get());

        return validCredentials;
    }

    void setActiveUser(User user) {
        activeUserStore.setActiveUser(user);
    }

}
