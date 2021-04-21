package com.epam.training.ticketservice.data.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
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
public class Movie {

    @Id
    private String title;
    private String genre;
    private int length;

    @OneToMany(mappedBy = "movie",
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Screening> screeningList;

    @Override
    public String toString() {
        return  title + "( " + genre + ", " + length + " minutes)";
    }
}
