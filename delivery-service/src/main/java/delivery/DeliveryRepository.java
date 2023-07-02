package delivery;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import delivery.model.Delivery;
import reactor.core.publisher.Flux;

@Repository
public interface DeliveryRepository extends ReactiveCrudRepository<Delivery, Integer> {
    Flux<Delivery> findByOrderId(Integer orderId);
}
