package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.order.OrderListDTO;
import bg.softuni.onlinebookstorebackend.model.error.OrderNotFoundException;
import bg.softuni.onlinebookstorebackend.service.OrderService;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import bg.softuni.onlinebookstorebackend.user.BookstoreUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/processed")
    public ResponseEntity<List<OrderListDTO>> getProcessedOrders() {
        return ResponseEntity.ok(orderService.getProcessedOrders());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/unprocessed")
    public ResponseEntity<List<OrderListDTO>> getUnprocessedOrders() {
        return ResponseEntity.ok(orderService.getUnprocessedOrders());
    }


    //@PreAuthorize("@orderService.isOwner(#principal.username, #id) or #principal.admin")
    @PreAuthorize("isOwner(#id)")
    @GetMapping("/{id}/details")
    public ResponseEntity<Object> getOrderDetails(@PathVariable("id") UUID id,
                                  @AuthenticationPrincipal BookstoreUserDetails principal) {
        Map<Long, Integer> orderItems = orderService.getOrderItems(id);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("orderId", id);
        body.put("items", orderItems);
        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Object> confirmOrder(@PathVariable("id") UUID id) {
        orderService.confirmOrder(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("Order %s confirmed", id));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<OrderListDTO>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getLoggedUserOrders(userDetails));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<Integer> getOrdersStatistics() {
        return ResponseEntity.ok(orderService.getNewOrdersCount());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<Object> onOrderNotFound(OrderNotFoundException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("Order %s not found", ex.getId()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
