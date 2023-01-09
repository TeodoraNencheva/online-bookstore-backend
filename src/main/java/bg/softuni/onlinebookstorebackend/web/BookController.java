package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.service.AuthorService;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.GenreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model,
                              @PageableDefault(
                                      sort = "title",
                                      direction = Sort.Direction.ASC,
                                      size = 5) Pageable pageable) {
        model.addAttribute("books", bookService.getAllBooks(pageable));
        model.addAttribute("title", "All books");
        model.addAttribute("type", "all");

        return "books";
    }

    @GetMapping("/{genre}")
    public String getBooksByGenre(@PathVariable("genre") String genre, Model model,
                                  @PageableDefault(
                                          sort = "title",
                                          direction = Sort.Direction.ASC,
                                          size = 5) Pageable pageable) {
        model.addAttribute("books", bookService.getBooksByGenre(genre, pageable));
        model.addAttribute("title", genre);
        model.addAttribute("type", genre);


        return "books";
    }

    @GetMapping("/{id}/details")
    public String getBookDetails(@PathVariable("id") Long id, Model model) {
        BookDetailsDTO bookDetails = bookService.getBookDetails(id);
        if (bookDetails == null) {
            throw new BookNotFoundException(id);
        }

        model.addAttribute("book", bookDetails);
        return "book-details";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        if (!model.containsAttribute("bookModel")) {
            model.addAttribute("bookModel", new AddNewBookDTO());
        }

        model.addAttribute("action", "/books/add");
        model.addAttribute("buttonText", "Add Book");
        model.addAttribute("title", "Add New Book");
        model.addAttribute("method", "post");
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
        return "book-add-or-update";
    }

    @PostMapping("/add")
    public String addBook(@Valid AddNewBookDTO bookModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bookModel", bookModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookModel",
                    bindingResult);
            return "redirect:/books/add";
        }

        BookEntity newBook = this.bookService.addNewBook(bookModel);

        return String.format("redirect:/books/%d/details", newBook.getId());
    }

    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable("id") Long id, Model model) {
        AddNewBookDTO bookModel = bookService.getBookById(id);

        if (bookModel == null) {
            throw new BookNotFoundException(id);
        }

        if (!model.containsAttribute("bookModel")) {
            model.addAttribute("bookModel", bookModel);
        }
        model.addAttribute("action", "/books/" + id);
        model.addAttribute("buttonText", "Update Book");
        model.addAttribute("title", "Update Book");
        model.addAttribute("method", "put");
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
        return "book-add-or-update";
    }

    @PutMapping("/{id}")
    public String updateBook(@Valid AddNewBookDTO bookModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bookModel", bookModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookModel",
                    bindingResult);
            return "redirect:/books/update/{id}";
        }

        BookEntity updatedBook = this.bookService.updateBook(bookModel, id);
        if (updatedBook == null) {
            throw new BookNotFoundException(id);
        }

        return String.format("redirect:/books/%d/details", updatedBook.getId());
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        if (bookService.getBookById(id) == null) {
            throw new BookNotFoundException(id);
        }

        bookService.deleteBook(id);
        return "redirect:/books/all";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @ModelAttribute SearchDTO searchDTO) {
        model.addAttribute("title", "Search for a book");
        model.addAttribute("action", "/books/search");

        if (!model.containsAttribute("searchDTO")) {
            model.addAttribute("searchDTO", new SearchDTO());
        }

        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            model.addAttribute("books", bookService.searchBooks(searchDTO));
        }

        return "search";
    }
}
