package ucb.group6.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The title field cannot be empty")
    private String title;

    @NotBlank(message = "The author field cannot be empty")
    private String author;

    @NotBlank(message = "The category field cannot be empty")
    private String category;

    private boolean available = true; // indica se o livro está disponível para empréstimo
}
