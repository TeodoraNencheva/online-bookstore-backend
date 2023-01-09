package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddBookToCartDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookAddedToCartDTO;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class CartRestController {
    private final UserService userService;
    private final BookService bookService;

    public CartRestController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookAddedToCartDTO> addBookToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody AddBookToCartDTO bookDTO) {
        if (userService.addBookToCart(userDetails, bookDTO)) {
            return ResponseEntity.ok(bookService.getAddedBook(bookDTO));
        }

        return ResponseEntity.notFound().build();
    }
}
