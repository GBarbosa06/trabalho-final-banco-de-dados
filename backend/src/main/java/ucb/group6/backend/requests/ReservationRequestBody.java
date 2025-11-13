package ucb.group6.backend.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequestBody {

    @NotNull(message = "The userId field cannot be null")
    private Long userId;

    @NotNull(message = "The bookId field cannot be null")
    private Long bookId;

    @NotNull(message = "The reservationDate field cannot be null")
    private LocalDate reservationDate;

    @NotNull(message = "The expirationDate field cannot be null")
    private LocalDate expirationDate;
}
