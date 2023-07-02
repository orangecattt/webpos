package delivery;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import delivery.model.Delivery;
import delivery.model.DeliveryField;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Configuration
public class DeliveryService {

    public class ExceptionDeliveryNotExist extends Exception {
    }

    @Autowired
    DeliveryRepository deliveryRepository;

    @Bean
    public Consumer<DeliveryField> addDelivery() {
        return field -> {
            Delivery delivery = new Delivery(field);
            deliveryRepository.save(delivery).subscribe();
            System.out.println("create a delivery");
        };
    }

    public Flux<Delivery> getDeliveryFromOrderId(Integer orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public Flux<Delivery> getDeliveries() {
        return deliveryRepository.findAll();
    }

    public Mono<Delivery> getDelivery(Integer id) {
        return deliveryRepository.findById(id);
    }

}
