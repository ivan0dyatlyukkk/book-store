package org.diatliuk.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import org.diatliuk.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o "
            + "LEFT JOIN o.user "
            + "LEFT JOIN o.orderItems")
    List<Order> findAll();

    @Query("FROM Order o "
            + "LEFT JOIN o.user "
            + "LEFT JOIN o.orderItems "
            + "WHERE o.id = :id")
    Optional<Order> findById(Long id);
}
