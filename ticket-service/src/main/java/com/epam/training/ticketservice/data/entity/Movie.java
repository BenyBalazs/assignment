package com.epam.training.ticketservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    @OneToMany(mappedBy = "movie")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Screening> screeningList;

    @ManyToMany(mappedBy = "attachedMovies")
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<PriceComponent> attachedComponents;

    @Override
    public String toString() {
        return  title + " (" + genre + ", " + length + " minutes)";
    }
}
