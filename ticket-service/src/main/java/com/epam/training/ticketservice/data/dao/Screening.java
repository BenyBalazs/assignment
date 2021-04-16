package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Screening {

    @EmbeddedId
    ScreeningPk screeningPk;
    LocalDate endOfScreening;

    @MapsId("title")
    @JoinColumns({
            @JoinColumn(name="title", referencedColumnName="title")
    })
    @ManyToOne
    private Movie movie;

    @MapsId("roomName")
    @JoinColumns({
            @JoinColumn(name="roomName", referencedColumnName="roomName")
    })
    @ManyToOne
    private Room room;
}
