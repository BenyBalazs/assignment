package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.dao.Screening;
import com.epam.training.ticketservice.data.dao.ScreeningPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ScreeningRepository extends JpaRepository<Screening, ScreeningPk> {
}
