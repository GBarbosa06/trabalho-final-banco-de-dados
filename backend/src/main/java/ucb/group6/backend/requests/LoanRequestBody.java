package ucb.group6.backend.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LoanRequestBody {

    @NotNull(message = "The userId field cannot be null")
    private Long userId;

    @NotNull(message = "The bookId field cannot be null")
    private Long bookId;

    @NotNull(message = "The loanDate field cannot be null")
    private LocalDate loanDate;

    @NotNull(message = "The returnDate field cannot be null")
    private LocalDate returnDate;
}
