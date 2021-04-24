package com.epam.training.ticketservice.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SeatIntPair {

    private int row;
    private int column;

    @Override
    public String toString() {
        return "(" + row + "," + column + ")";
    }
}
