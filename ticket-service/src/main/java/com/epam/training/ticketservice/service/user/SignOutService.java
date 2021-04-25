package com.epam.training.ticketservice.service.user;

import com.epam.training.ticketservice.utils.ActiveUserStore;
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
