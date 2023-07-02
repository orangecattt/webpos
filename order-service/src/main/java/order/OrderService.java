package order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import order.model.Order;
import order.model.OrderField;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    public class ExceptionNoOrder extends Exception {
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StreamBridge streamBridge;

    public Flux<Order> addOrders(OrderField[] items) {
        return orderRepository.saveAll(Flux.fromArray(items)
                .map(orderField -> {
                    Order order = new Order();
                    order.setField(orderField);
                    return order;
                }))
            .doOnNext(order -> {
                streamBridge.send("order-out-0", order);
            });
    }

    public Flux<Order> getOrders(){
        return orderRepository.findAll();
    }

    public Mono<Order> getOrder(Integer orderId) {
        return orderRepository.findById(orderId);
    }

}
