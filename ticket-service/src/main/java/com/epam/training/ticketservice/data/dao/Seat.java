package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer rowPosition;
    Integer colPosition;

    @ManyToOne
    @JoinColumn(name = "room")
    Room room;
}
