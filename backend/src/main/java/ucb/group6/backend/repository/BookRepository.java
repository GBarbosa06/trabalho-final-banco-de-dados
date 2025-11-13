package ucb.group6.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.group6.backend.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
