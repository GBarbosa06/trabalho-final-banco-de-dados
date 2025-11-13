package ucb.group6.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.group6.backend.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
