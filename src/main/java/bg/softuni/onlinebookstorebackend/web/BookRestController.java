package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.model.validation.ExistingAuthorId;
import bg.softuni.onlinebookstorebackend.model.validation.ExistingBookId;
import bg.softuni.onlinebookstorebackend.model.validation.ExistingGenre;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.GenreService;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Validated
public class BookRestController {
    private final BookService bookService;
    private final GenreService genreService;

    @GetMapping("/all")
    public ResponseEntity<List<BookOverviewDTO>> getAllBooks(
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/all/count")
    public ResponseEntity<Long> getAllBooksCount() {
        return ResponseEntity.ok(bookService.getAllBooksCount());
    }

    @GetMapping("/all-genres")
    public ResponseEntity<List<GenreEntity>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{genre}")
    public ResponseEntity<List<BookOverviewDTO>> getBooksByGenre(@ExistingGenre @PathVariable("genre") String genre,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return ResponseEntity.ok(bookService.getBooksByGenre(genre, pageable));
    }

    @GetMapping("/{genre}/count")
    public ResponseEntity<Long> getBooksCountByGenre(@ExistingGenre @PathVariable("genre") String genre) {
        return ResponseEntity.ok(bookService.getBooksCountByGenre(genre));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@ExistingBookId @PathVariable("id") Long id) {
        BookDetailsDTO bookDetails = bookService.getBookDetails(id);
        return ResponseEntity.ok(bookDetails);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> addBook(@Valid @RequestPart(name = "bookModel") AddNewBookDTO bookModel,
                                              @RequestPart(name = "picture", required = false) MultipartFile picture) throws IOException {

        BookEntity newBook = this.bookService.addNewBook(bookModel, picture);
        return ResponseEntity.ok(newBook);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> updateBook(@Valid @RequestPart(name = "bookModel") AddNewBookDTO bookModel,
                                                 @RequestPart(name = "picture", required = false) MultipartFile picture,
                                                 @ExistingBookId @PathVariable("id") Long id) throws IOException {
        BookEntity updatedBook = this.bookService.updateBook(bookModel, id, picture);
        return ResponseEntity.ok(updatedBook);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@ExistingBookId @PathVariable("id") Long id) {
        bookService.deleteBook(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("Book with ID %s deleted", id));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<BookOverviewDTO>> search(@RequestBody SearchDTO searchDTO) {

        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooks(searchDTO));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<BookOverviewDTO>> getBooksByAuthor(
            @ExistingAuthorId @RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }
}
