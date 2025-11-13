package ucb.group6.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull(message = "Book cannot be null")
    private Book book;

    private LocalDate reservationDate = LocalDate.now();

    private boolean active = true; // reserva ativa até o empréstimo ou cancelamento
}
