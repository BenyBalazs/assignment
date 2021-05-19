package com.epam.training.ticketservice.service.utils;

import com.epam.training.ticketservice.data.entity.Screening;
import com.epam.training.ticketservice.service.interfaces.ScreeningHelperUtilityInterface;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScreeningValidator implements ScreeningHelperUtilityInterface {

    private static final int BRAKE_PERIOD_TIME_BETWEEN_SCREENINGS = 10;

    @Override
    public boolean isOverlappingScreening(List<Screening> screeningsInARoom, LocalDateTime startOfScreening) {
        return screeningsInARoom.stream()
                .anyMatch(screening -> betweenTwoDates(startOfScreening,
                        screening.getStartOfScreening(), screening.getEndOfScreening()));
    }

    @Override
    public boolean screeningInTheBrakePeriod(List<Screening> screeningsInARoom, LocalDateTime startOfScreening) {
        return screeningsInARoom.stream()
                .anyMatch(screening -> betweenTwoDates(startOfScreening, screening.getEndOfScreening(),
                        addMinutesToDateTime(screening.getEndOfScreening(), BRAKE_PERIOD_TIME_BETWEEN_SCREENINGS)));
    }

    @Override
    public boolean betweenTwoDates(LocalDateTime questioned, LocalDateTime small, LocalDateTime big) {
        return small.compareTo(questioned) * questioned.compareTo(big) >= 0;
    }

    @Override
    public LocalDateTime addMinutesToDateTime(LocalDateTime start, int length) {
        return start.plusMinutes(length);
    }
}
