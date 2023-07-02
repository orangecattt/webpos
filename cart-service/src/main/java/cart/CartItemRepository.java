package cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import cart.model.CartItem;

public interface CartItemRepository extends ReactiveCrudRepository<CartItem, Integer> {
}
