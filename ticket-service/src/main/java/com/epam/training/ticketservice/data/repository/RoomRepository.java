package com.epam.training.ticketservice.data.repository;

import com.epam.training.ticketservice.data.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoomRepository extends JpaRepository<Room, String> {
}
