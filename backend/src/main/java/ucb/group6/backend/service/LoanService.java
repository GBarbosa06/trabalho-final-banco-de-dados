package ucb.group6.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ucb.group6.backend.model.*;
import ucb.group6.backend.repository.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public List<Loan> listAll() {
        return loanRepository.findAll();
    }

    public Loan createLoan(Loan loan) {
        Book book = loan.getBook();

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for loan");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now().plusDays(7)); // devolução em 7 dias

        return loanRepository.save(loan);
    }

    public Loan returnBook(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setReturned(true);
        loan.getBook().setAvailable(true);

        bookRepository.save(loan.getBook());
        return loanRepository.save(loan);
    }
}
