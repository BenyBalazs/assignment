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
            name = "linked_price_components",
            joinColumns = @JoinColumn(name = "attached_room"),
            inverseJoinColumns = @JoinColumn(name = "priceComponentName")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    List<Room> attachedRooms;

    @ManyToMany
    @JoinTable(
            name = "linked_price_components",
            joinColumns = @JoinColumn(name = "attached_movie"),
            inverseJoinColumns = @JoinColumn(name = "title")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    List<Room> attachedMovies;
}
