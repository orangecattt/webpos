package order;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import order.model.Order;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Integer> {
    
}
