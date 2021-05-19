package com.epam.training.ticketservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @JoinColumn(name = "attachedMovie")
    @ManyToOne
    private Movie attachedMovie;
    @JoinColumn(name = "attachedScreening")
    @ManyToOne
    private Screening attachedScreening;
    @JoinColumn(name = "attachedRoom")
    @ManyToOne
    private Room attachedRoom;


    @Override
    public String toString() {
        return "PriceComponent{"
                + "priceComponentName='" + priceComponentName + '\''
                + ", price=" + price
                + '}';
    }
}
