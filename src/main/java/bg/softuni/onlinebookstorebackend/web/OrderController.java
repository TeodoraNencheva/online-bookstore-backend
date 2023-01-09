package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.error.OrderNotFoundException;
import bg.softuni.onlinebookstorebackend.service.OrderService;
import bg.softuni.onlinebookstorebackend.user.BookstoreUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/processed")
    public String getProcessedOrders(Model model) {
        model.addAttribute("type", "Processed");
        model.addAttribute("orders", orderService.getProcessedOrders());
        return "orders";
    }

    @GetMapping("/unprocessed")
    public String getUnprocessedOrders(Model model) {
        model.addAttribute("type", "Unprocessed");
        model.addAttribute("orders", orderService.getUnprocessedOrders());

        return "orders";
    }


    //@PreAuthorize("@orderService.isOwner(#principal.username, #id) or #principal.admin")
    @PreAuthorize("isOwner(#id)")
    @GetMapping("/{id}/details")
    public String getOrderDetails(@PathVariable("id") UUID id, Model model,
                                  @AuthenticationPrincipal BookstoreUserDetails principal) {
        model.addAttribute("order", orderService.getOrder(id));
        model.addAttribute("items", orderService.getOrderItems(id));
        return "order-details";
    }

    @PostMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable("id") UUID id) {
        orderService.confirmOrder(id);
        return "redirect:/orders/unprocessed";
    }

    @GetMapping("/mine")
    public String getMyOrders(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        model.addAttribute("type", "My");
        model.addAttribute("orders", orderService.getLoggedUserOrders(userDetails));
        return "orders";
    }

    @GetMapping("/statistics")
    public String getOrdersStatistics(Model model) {
        model.addAttribute("count", orderService.getNewOrdersCount());
        return "orders-statistics";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({OrderNotFoundException.class})
    public ModelAndView onOrderNotFound(OrderNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("title", "Order not found");
        modelAndView.addObject("message", String.format("Order with id %s not found", ex.getId()));

        return modelAndView;
    }
}
