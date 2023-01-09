package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @Tag(name = "Get books by author", description = "Returns all books by a given author ID")
    @Parameter(name = "authorId", description = "The id of the author of the books",
            required = true)
    @ApiResponse(responseCode = "200", description = "If the author id is valid")
    @ApiResponse(responseCode = "404", description = "If the author id is invalid")
    @GetMapping
    public ResponseEntity<List<BookOverviewDTO>> getBooksByAuthor(
            @RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @ExceptionHandler({AuthorNotFoundException.class})
    public ResponseEntity<String> onAuthorNotFound(AuthorNotFoundException ex) {
        return ResponseEntity.status(404).body(String.format("Author with id %d does not exist", ex.getId()));
    }
}
