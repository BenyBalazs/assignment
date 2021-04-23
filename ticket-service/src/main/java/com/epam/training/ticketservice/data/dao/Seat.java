package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"room","rowPosition","colPosition"}))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    long seatPk;
    Integer rowPosition;
    Integer colPosition;

    @ManyToOne
    @JoinColumn(name = "roomName")
    Room room;

    @OneToMany(mappedBy = "seat")
    @EqualsAndHashCode.Exclude
    List<Ticket> tickets;
}
