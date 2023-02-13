package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddBookToCartDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookAddedToCartDTO;
import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import bg.softuni.onlinebookstorebackend.model.error.EmptyCartException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.OrderService;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import bg.softuni.onlinebookstorebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartRestController {
    private final UserService userService;
    private final OrderService orderService;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookAddedToCartDTO>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserCart(userDetails));
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

        Map<String, Object> body = ResponseService.generateGeneralResponse(
                String.format("Book with ID %s removed from cart", bookId));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<Object> removeAllItems(@AuthenticationPrincipal UserDetails userDetails) {
        userService.removeAllItemsFromCart(userDetails);

        Map<String, Object> body = ResponseService.generateGeneralResponse("All books removed from cart.");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Object> confirmOrder(@AuthenticationPrincipal UserDetails userDetails,
                                               UriComponentsBuilder uriComponentsBuilder) {
        OrderEntity newOrder = orderService.createNewOrder(userDetails);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/orders/{id}/details")
                        .build(newOrder.getId()))
                .build();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EmptyCartException.class})
    public ResponseEntity<Object> onEmptyCart(EmptyCartException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
