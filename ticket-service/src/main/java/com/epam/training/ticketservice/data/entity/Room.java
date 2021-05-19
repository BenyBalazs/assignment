package com.epam.training.ticketservice.data.entity;

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
    private List<Seat> seats;

    @OneToMany(mappedBy = "roomOfScreening")
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Screening> screeningList;

    @OneToMany(mappedBy = "attachedRoom",
            cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<PriceComponent> attachedComponents;

    public int getMaxRows() {
        return seats.stream().mapToInt(Seat::getRowPosition).max().orElse(0);
    }

    public int getMaxCols() {
        return seats.stream().mapToInt(Seat::getColPosition).max().orElse(0);
    }

    @Override
    public String toString() {
        int maxRows = getMaxRows();
        int maxCols = getMaxCols();
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
