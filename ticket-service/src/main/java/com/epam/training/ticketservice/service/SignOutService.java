package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.ActiveUserStore;
import com.epam.training.ticketservice.service.interfaces.SignOutInterface;
import org.springframework.stereotype.Component;

@Component
public class SignOutService implements SignOutInterface {

    ActiveUserStore activeUserStore;

    public SignOutService(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    @Override
    public boolean singOut() {

        activeUserStore.setActiveUser(null);

        return true;
    }
}
