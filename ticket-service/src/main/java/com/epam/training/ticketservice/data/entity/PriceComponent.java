package com.epam.training.ticketservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
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

    @ManyToMany(targetEntity = Room.class, cascade = CascadeType.ALL)
    @JoinTable(
            name = "linked_price_components",
            joinColumns = @JoinColumn(name = "attached_room"),
            inverseJoinColumns = @JoinColumn(name = "priceComponentName")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Room> attachedRooms;

    @ManyToMany(targetEntity = Movie.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
            name = "linked_price_components",
            joinColumns = @JoinColumn(name = "attached_movie"),
            inverseJoinColumns = @JoinColumn(name = "title")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Movie> attachedMovies;

    @ManyToMany(targetEntity = Screening.class,
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
            name = "linked_price_components",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "title")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private List<Screening> attachedScreenings;

    @Override
    public String toString() {
        return "PriceComponent{"
                + "priceComponentName='" + priceComponentName + '\''
                + ", price=" + price
                + '}';
    }
}
