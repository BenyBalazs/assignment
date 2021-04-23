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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"booked_seat","screening"}))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    long ticketPk;
    @EqualsAndHashCode.Exclude
    int ticketPrice;
    @ManyToOne
    @JoinColumn(name = "booked_seat")
    Seat seat;
    @ManyToOne
    @JoinColumn(name = "user_pk")
    @EqualsAndHashCode.Exclude
    User user;
    @ManyToOne
    @JoinColumn(name = "screening")
    Screening screening;
}
