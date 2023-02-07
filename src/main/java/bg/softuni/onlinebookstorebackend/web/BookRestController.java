package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.GenreService;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
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

    @GetMapping("/all-genres")
    public ResponseEntity<List<GenreEntity>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{genre}")
    public ResponseEntity<List<BookOverviewDTO>> getBooksByGenre(@PathVariable("genre") String genre,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return ResponseEntity.ok(bookService.getBooksByGenre(genre, pageable));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable("id") Long id) {
        BookDetailsDTO bookDetails = bookService.getBookDetails(id);
        return ResponseEntity.ok(bookDetails);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> addBook(@Valid @RequestPart(name = "bookModel") AddNewBookDTO bookModel,
                                              @RequestPart(name = "picture", required = false) MultipartFile picture,
                                              UriComponentsBuilder uriComponentsBuilder) throws IOException {
        bookModel.setPicture(picture);
        BookEntity newBook = this.bookService.addNewBook(bookModel);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/books/{id}/details")
                        .build(newBook.getId()))
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> updateBook(@Valid @RequestPart(name = "bookModel") AddNewBookDTO bookModel,
                                                 @RequestPart(name = "picture", required = false) MultipartFile picture,
                                                 @PathVariable("id") Long id) throws IOException {
        bookModel.setPicture(picture);
        BookEntity updatedBook = this.bookService.updateBook(bookModel, id);
        return ResponseEntity.ok(updatedBook);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") Long id) {
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
            @RequestParam(value = "authorId") Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }
}
