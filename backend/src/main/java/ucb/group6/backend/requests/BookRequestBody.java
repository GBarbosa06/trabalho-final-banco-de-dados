package ucb.group6.backend.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequestBody {

    @NotEmpty(message = "The title field cannot be empty")
    private String title;

    @NotEmpty(message = "The author field cannot be empty")
    private String author;

    @NotEmpty(message = "The genre field cannot be empty")
    private String genre;

    @NotNull(message = "The quantity field cannot be null")
    private Integer quantity;
}
