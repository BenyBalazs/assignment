package com.epam.training.ticketservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatIntPair {

    private int row;
    private int column;

    @Override
    public String toString() {
        return "(" + row + "," + column + ")";
    }
}
