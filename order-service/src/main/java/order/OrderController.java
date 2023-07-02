package order;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import order.model.Order;
import order.model.OrderField;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public Mono<ResponseEntity<List<Order>>> addOrders(@RequestBody OrderField[] items){
        return orderService.addOrders(items)
            .collectList()
            .map(orders -> ResponseEntity.ok(orders));
    }

    @GetMapping("/orders")
    public Mono<ResponseEntity<List<Order>>> getOrders(){
        return orderService.getOrders()
            .collectList()
            .map(orders -> ResponseEntity.ok(orders));
    }

    @GetMapping("/orders/{orderId}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable("orderId") Integer orderId){
        return orderService.getOrder(orderId)
            .map(order -> ResponseEntity.ok(order))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
