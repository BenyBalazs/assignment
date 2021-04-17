package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room {

    @Id
    private String roomName;

    @OneToMany(mappedBy = "room")
    List<Seat> seats;

    @OneToMany(mappedBy = "roomOfScreening",
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<Screening> screeningList;
}
