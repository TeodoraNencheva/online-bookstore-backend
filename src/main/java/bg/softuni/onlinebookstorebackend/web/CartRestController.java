package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddBookToCartDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookAddedToCartDTO;
import bg.softuni.onlinebookstorebackend.model.error.EmptyCartException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.OrderService;
import bg.softuni.onlinebookstorebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class CartRestController {
    private final UserService userService;
    private final OrderService orderService;
    private final BookService bookService;

    public CartRestController(UserService userService, OrderService orderService, BookService bookService) {
        this.userService = userService;
        this.orderService = orderService;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Map<Long, Integer>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Long, Integer> userCart = userService.getUserCart(userDetails);
        return ResponseEntity.ok(userCart);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookAddedToCartDTO> addBookToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody AddBookToCartDTO bookDTO) {
        if (userService.addBookToCart(userDetails, bookDTO)) {
            return ResponseEntity.ok(bookService.getAddedBook(bookDTO));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Object> removeItem(@PathVariable("id") Long bookId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        userService.removeItemFromCart(bookId, userDetails);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", String.format("Book with ID %s removed from cart", bookId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<Object> removeAllItems(@AuthenticationPrincipal UserDetails userDetails) {
        userService.removeAllItemsFromCart(userDetails);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "All books removed from cart.");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Object> confirmOrder(@AuthenticationPrincipal UserDetails userDetails) {
        orderService.createNewOrder(userDetails);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "New order created.");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EmptyCartException.class})
    public ResponseEntity<Object> onEmptyCart(EmptyCartException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
