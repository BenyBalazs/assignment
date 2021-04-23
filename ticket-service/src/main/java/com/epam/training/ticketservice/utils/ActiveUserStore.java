package com.epam.training.ticketservice.utils;

import com.epam.training.ticketservice.data.dao.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ActiveUserStore {

    private User activeUser;

    public ActiveUserStore() {
        this.activeUser = null;
    }
}
