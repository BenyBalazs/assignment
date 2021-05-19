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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class PriceComponent {

    @Id
    String priceComponentName;
    int price;

    @ManyToMany
    @JoinTable(
            name = "room_price_components",
            joinColumns = @JoinColumn(name = "priceComponentName"),
            inverseJoinColumns = @JoinColumn(name = "roomName")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Room> attachedRooms;

    @ManyToMany
    @JoinTable(
            name = "movie_price_components",
            joinColumns = @JoinColumn(name = "priceComponentName"),
            inverseJoinColumns = @JoinColumn(name = "title")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Movie> attachedMovies;

    @ManyToMany
    @JoinTable(
            name = "screening_price_components",
            joinColumns = @JoinColumn(name = "priceComponentName"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Screening> attachedScreenings;

    @Override
    public String toString() {
        return "PriceComponent{" +
                "priceComponentName='" + priceComponentName + '\'' +
                ", price=" + price +
                '}';
    }
}
