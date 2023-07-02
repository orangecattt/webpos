package delivery;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import delivery.model.Delivery;
import reactor.core.publisher.Mono;

@RestController
public class DeliveryController {
    
    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/deliveries")
    public Mono<ResponseEntity<List<Delivery>>> getDeliveries(
        @RequestParam(name = "orderId", required = false) Integer orderId
    ){
        if (orderId == null){
            return deliveryService.getDeliveries()
                .collectList()
                .map(deliveries -> ResponseEntity.ok(deliveries));
        }
        else{
            return deliveryService.getDeliveryFromOrderId(orderId)
                .collectList()
                .flatMap(deliveries -> {
                    if (deliveries.size() == 0){
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    else{
                        return Mono.just(ResponseEntity.ok(deliveries));
                    }
            });
        }
    }

    @GetMapping("/deliveries/{deliveryId}")
    public Mono<ResponseEntity<Delivery>> getDelivery(@PathVariable("deliveryId") Integer deliveryId){
        return deliveryService.getDelivery(deliveryId)
            .map(delivery -> ResponseEntity.ok(delivery))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
