package ucb.group6.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.group6.backend.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
