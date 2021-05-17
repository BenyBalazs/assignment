package com.epam.training.ticketservice.service.interfaces;

import com.epam.training.ticketservice.data.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningHelperUtilityInterface {

    boolean isOverlappingScreening(List<Screening> screeningsInARoom, LocalDateTime startOfScreening);

    boolean screeningInTheBrakePeriod(List<Screening> screeningsInARoom, LocalDateTime startOfScreening);

    boolean betweenTwoDates(LocalDateTime questioned, LocalDateTime small, LocalDateTime big);

    LocalDateTime addMinutesToDateTime(LocalDateTime start, int length);
}
