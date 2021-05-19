package com.epam.training.ticketservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"movie","roomOfscreening","startOfScreening"}))
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @JoinColumn(name = "movie")
    @ManyToOne
    private Movie movie;
    @JoinColumn(name = "roomOfscreening")
    @ManyToOne
    private Room roomOfScreening;
    @Column(nullable = false)
    private LocalDateTime startOfScreening;

    @ManyToMany(mappedBy = "attachedScreenings")
    @EqualsAndHashCode.Exclude
    private List<PriceComponent> attachedComponents;

    public LocalDateTime getEndOfScreening() {
        return startOfScreening.plusMinutes(movie.getLength());
    }

    @Override
    public String toString() {
        return movie.toString()
                + ", screened in room "
                + roomOfScreening.getRoomName()
                + ", at "
                + startOfScreening.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
