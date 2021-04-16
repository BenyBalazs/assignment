package com.epam.training.ticketservice.data.dao;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
public class ScreeningPk implements Serializable {

    private String title;
    private String roomName;
    private LocalDate startOfScreening;
}
