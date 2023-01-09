package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.error.EmptyCartException;
import bg.softuni.onlinebookstorebackend.service.OrderService;
import bg.softuni.onlinebookstorebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final UserService userService;
    private final OrderService orderService;

    public CartController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Map<BookEntity, Integer> userCart = userService.getUserCart(userDetails);
        model.addAttribute("cart", userCart);

        return "cart";
    }

    @DeleteMapping("/{id}/remove")
    public String removeItem(@PathVariable("id") Long bookId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        userService.removeItemFromCart(bookId, userDetails);

        return "redirect:/cart";
    }

    @DeleteMapping("/removeAll")
    public String removeAllItems(@AuthenticationPrincipal UserDetails userDetails) {
        userService.removeAllItemsFromCart(userDetails);

        return "redirect:/cart";
    }

    @PostMapping("/confirm")
    public String confirmOrder(@AuthenticationPrincipal UserDetails userDetails) {
        orderService.createNewOrder(userDetails);

        return "redirect:/cart";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyCartException.class})
    public ModelAndView onEmptyCart(EmptyCartException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("title", "Cart is empty");
        modelAndView.addObject("message", ex.getMessage());

        return modelAndView;
    }

}
