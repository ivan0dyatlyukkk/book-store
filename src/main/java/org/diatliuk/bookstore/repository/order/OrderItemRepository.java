package org.diatliuk.bookstore.repository.order;

import java.util.List;
import org.diatliuk.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByOrder_Id(Long id);
}
