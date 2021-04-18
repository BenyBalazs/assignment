package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.data.dao.User;
import com.epam.training.ticketservice.data.repository.UserRepository;
import com.epam.training.ticketservice.exception.UserAlreadyLoggedInException;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractLogIn implements LogInInterface {

    UserRepository userRepository;
    ActiveUserStore activeUserStore;

    public AbstractLogIn(UserRepository userRepository, ActiveUserStore activeUserStore) {
        this.userRepository = userRepository;
        this.activeUserStore = activeUserStore;
    }

    public boolean credentialCheck(String username, String password) throws UserAlreadyLoggedInException {

        User user = getUser(username);

        if (user == null) {
            return false;
        }

        if (activeUserStore.getActiveUser() != null) {
            throw new UserAlreadyLoggedInException();
        }
        boolean validCredentials = user.getPassword().equals(password);

        if (validCredentials) {
            setActiveUser(user);
        }

        return validCredentials;
    }

    public User getUser(String username) {
        return userRepository.findById(username).orElse(null);
    }

    private void setActiveUser(User user) {
        activeUserStore.setActiveUser(user);
    }
}
