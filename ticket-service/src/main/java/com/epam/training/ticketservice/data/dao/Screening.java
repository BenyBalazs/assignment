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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"movie","roomOfscreening","startOfScreening"}))
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @JoinColumn(name = "movie")
    @ManyToOne
    private Movie movie;
    @JoinColumn(name = "roomOfscreening")
    @ManyToOne
    private Room roomOfScreening;
    private LocalDate startOfScreening;
    private LocalDate endOfScreening;
}
