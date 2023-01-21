package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.dto.order.OrderListDTO;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import bg.softuni.onlinebookstorebackend.model.entity.UserEntity;
import bg.softuni.onlinebookstorebackend.model.error.EmptyCartException;
import bg.softuni.onlinebookstorebackend.model.error.OrderNotFoundException;
import bg.softuni.onlinebookstorebackend.model.mapper.OrderMapper;
import bg.softuni.onlinebookstorebackend.repositories.OrderRepository;
import bg.softuni.onlinebookstorebackend.repositories.UserRepository;
import bg.softuni.onlinebookstorebackend.user.BookstoreUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private int newOrdersCount = 0;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public int getNewOrdersCount() {
        return newOrdersCount;
    }

    @Transactional
    public void createNewOrder(UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).get();
        if (user.getCart() == null || user.getCart().isEmpty()) {
            throw new EmptyCartException("Cannot create order on empty cart!");
        }

        Map<BookEntity, Integer> items = user.getCart();
        OrderEntity newOrder = new OrderEntity(user, items);
        orderRepository.save(newOrder);
        user.emptyCart();
        userRepository.save(user);
        newOrdersCount++;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<OrderListDTO> getUnprocessedOrders() {
        return orderRepository.getAllUnprocessed().stream()
                .map(orderMapper::orderEntityToOrderListDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<OrderListDTO> getProcessedOrders() {
        return orderRepository.getAllProcessed().stream()
                .map(orderMapper::orderEntityToOrderListDTO)
                .collect(Collectors.toList());
    }

    public OrderEntity getOrder(UUID id) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException(id);
        }

        return orderOpt.get();
    }

    @PreAuthorize("@orderService.isOwner(#principal.username, #id) or #principal.admin")
    @Transactional
    public Map<Long, Integer> getOrderItems(UUID id, BookstoreUserDetails principal) {
        Map<BookEntity, Integer> items = getOrder(id).getItems();
        Map<Long, Integer> result = new LinkedHashMap<>();

        for (BookEntity bookEntity : items.keySet()) {
            result.put(bookEntity.getId(), items.get(bookEntity));
        }

        return result;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void confirmOrder(UUID id) {
        OrderEntity order = getOrder(id);
        order.setProcessed(true);
        orderRepository.save(order);
    }


    public List<OrderListDTO> getLoggedUserOrders(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("");
        }

        return orderRepository.getAllByOwner_Email(userDetails.getUsername())
                .stream().map(orderMapper::orderEntityToOrderListDTO)
                .collect(Collectors.toList());
    }

    public void cleanNewOrdersCount() {
        newOrdersCount = 0;
    }

    public boolean isOwner(String userName, UUID id) {
        OrderEntity order = getOrder(id);

        Optional<UserEntity> userOpt = userRepository.findByEmail(userName);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(userName);
        }

        return order.getOwner().getEmail().equals(userName);
    }
}
