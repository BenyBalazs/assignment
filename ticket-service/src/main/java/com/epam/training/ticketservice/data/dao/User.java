package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    public static enum Role {
        ADMIN,
        USER,
    }

    @Id
    private String username;
    private String password;
    private Role role;



}
