package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room {

    @Id
    @Column(name = "roomName")
    private String roomName;

    @OneToMany(mappedBy = "room")
    List<Seat> seats;

    @OneToMany(mappedBy = "roomOfScreening",
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
            orphanRemoval = true)
    private List<Screening> screeningList;
}
