package org.diatliuk.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.order.CreateOrderRequestDto;
import org.diatliuk.bookstore.dto.order.OrderDto;
import org.diatliuk.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;
import org.diatliuk.bookstore.enums.Status;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.mapper.OrderItemMapper;
import org.diatliuk.bookstore.mapper.OrderMapper;
import org.diatliuk.bookstore.model.CartItem;
import org.diatliuk.bookstore.model.Order;
import org.diatliuk.bookstore.model.OrderItem;
import org.diatliuk.bookstore.model.User;
import org.diatliuk.bookstore.repository.book.BookRepository;
import org.diatliuk.bookstore.repository.cart.CartItemRepository;
import org.diatliuk.bookstore.repository.cart.ShoppingCartRepository;
import org.diatliuk.bookstore.repository.order.OrderItemRepository;
import org.diatliuk.bookstore.repository.order.OrderRepository;
import org.diatliuk.bookstore.service.OrderService;
import org.diatliuk.bookstore.service.ShoppingCartService;
import org.diatliuk.bookstore.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserService userService;
    private final ShoppingCartService cartService;
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public OrderDto save(CreateOrderRequestDto requestDto) {
        User authenticatedUser = userService.getAuthenticatedUser();
        ShoppingCartDto shoppingCartDto = cartService.get();

        Order order = createOrder(authenticatedUser, shoppingCartDto);
        order.setShippingAddress(requestDto.getShippingAddress());
        Order savedOrder = orderRepository.save(order);

        Set<CartItem> cartItems = cartRepository
                .getShoppingCartByUserId(authenticatedUser.getId())
                .getCartItems();
        List<OrderItem> orderItems = cartItems
                .stream()
                .map(orderItemMapper::cartItemToOrderItem)
                .peek(orderItem -> orderItem.setOrder(savedOrder))
                .toList();

        savedOrder.setOrderItems(Set.copyOf(orderItemRepository.saveAll(orderItems)));
        cartItemRepository.deleteAll(cartItems);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto update(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "an order by id: " + id));
        order.setStatus(Status.valueOf(requestDto.getStatus()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getItemsByOrderId(Long id) {
        return orderItemRepository.getOrderItemsByOrder_Id(id)
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getItemInOrderById(Long orderId, Long id) {
        OrderItem item = orderItemRepository.getOrderItemsByOrder_Id(orderId)
                .stream().filter(orderItem -> orderItem.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Can't find an orderItem with id: "
                                                + id + " in the order with id: " + orderId));
        return orderItemMapper.toDto(item);
    }

    private BigDecimal calculateTotalSum(ShoppingCartDto shoppingCartDto) {
        Double total = shoppingCartDto.getCartItems().stream()
                .map(item ->
                        item.getQuantity() * bookRepository.findById(item.getBookId())
                                                                        .get()
                                                                        .getPrice()
                                                                        .doubleValue())
                .reduce(0.0, Double::sum);
        return BigDecimal.valueOf(total);
    }

    private Order createOrder(User user, ShoppingCartDto shoppingCartDto) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.CREATED);
        order.setTotal(calculateTotalSum(shoppingCartDto));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(user.getShippingAddress());
        return order;
    }
}
