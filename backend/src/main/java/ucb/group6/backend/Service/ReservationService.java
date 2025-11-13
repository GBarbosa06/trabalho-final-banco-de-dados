package ucb.group6.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ucb.group6.backend.model.*;
import ucb.group6.backend.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(Reservation reservation) {
        Book book = reservation.getBook();

        if (!book.isAvailable()) {
            reservation.setActive(true);
            return reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("Book is available, no need to reserve");
        }
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setActive(false);
        reservationRepository.save(reservation);
    }
}
