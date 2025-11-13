package ucb.group6.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ucb.group6.backend.model.Book;
import ucb.group6.backend.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public void setAvailability(Long id, boolean available) {
        Book book = findById(id);
        if (book != null) {
            book.setAvailable(available);
            bookRepository.save(book);
        }
    }
}
