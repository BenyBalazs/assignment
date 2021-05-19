package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.entity.BasePrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePriceRepository extends JpaRepository<BasePrice, String> {
}
