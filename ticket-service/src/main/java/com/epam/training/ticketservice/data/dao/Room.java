package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Room {

    @Id
    @Column(name = "roomName")
    private String roomName;

    @OneToMany(mappedBy = "room",
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Seat> seats;
    int maxRows;
    int maxCols;

    @OneToMany(mappedBy = "roomOfScreening",
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Screening> screeningList;

    @Override
    public String toString() {
        return "Room "
                + roomName
                + " with "
                + maxRows * maxCols
                + " seats, "
                + maxRows
                + " rows and "
                + maxCols
                + " columns";
    }
}
