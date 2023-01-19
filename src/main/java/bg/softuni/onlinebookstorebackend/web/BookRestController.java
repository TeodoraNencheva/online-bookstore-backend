package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<Page<BookOverviewDTO>> getAllBooks(
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{genre}")
    public ResponseEntity<Page<BookOverviewDTO>> getBooksByGenre(@PathVariable("genre") String genre,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        return ResponseEntity.ok(bookService.getBooksByGenre(genre, pageable));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable("id") Long id) {
        BookDetailsDTO bookDetails = bookService.getBookDetails(id);
        if (bookDetails == null) {
            throw new BookNotFoundException(id);
        }

        return ResponseEntity.ok(bookDetails);
    }

    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> addBook(@Valid @RequestPart AddNewBookDTO bookModel,
                                              @RequestPart(required = false) MultipartFile picture) throws IOException {
        bookModel.setPicture(picture);
        BookEntity newBook = this.bookService.addNewBook(bookModel);

        return ResponseEntity.ok(newBook);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookEntity> updateBook(@Valid @RequestPart AddNewBookDTO bookModel,
                                                 @RequestPart(required = false) MultipartFile picture,
                                                 @PathVariable("id") Long id) throws IOException {
        bookModel.setPicture(picture);

        BookEntity updatedBook = this.bookService.updateBook(bookModel, id);
        if (updatedBook == null) {
            throw new BookNotFoundException(id);
        }

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteBook(@PathVariable("id") Long id) {
        if (bookService.getBookById(id) == null) {
            throw new BookNotFoundException(id);
        }

        bookService.deleteBook(id);

        GeneralResponse body = new GeneralResponse(String.format("Book with ID %s deleted", id));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<BookOverviewDTO>> search(@RequestBody SearchDTO searchDTO) {

        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooks(searchDTO));
        }

        return ResponseEntity.badRequest().build();
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
}