package gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gateway.model.CartItem;
import gateway.model.CartItemField;
import gateway.model.Delivery;
import gateway.model.Order;
import gateway.model.OrderField;
import gateway.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GatewayService {

    @Autowired
    WebClient.Builder builder;

    public final String productsServiceUrl = "http://product-service";

    public final String cartServiceUrl = "http://cart-service";

    public final String orderServiceUrl = "http://order-service";

    public final String deliveryServiceUrl = "http://delivery-service";

    @Data
    @AllArgsConstructor
    public class WebPosRequestException extends RuntimeException{
        HttpStatusCode code;
        String msg;
    };

    public Flux<Product> getProducts(Integer page, Integer size) {
        return builder.build().get().uri(productsServiceUrl + "/products?page={page}&size={size}", page, size).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.value() == HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error -> {
                    return Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding"));
            })
            .bodyToFlux(Product.class);
    }

    public Mono<Product> getProduct(String productId) {
        return builder.build().get().uri(productsServiceUrl + "/products/{productId}", productId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(Product.class);
    }

    public Flux<CartItem> getCart() {
        return builder.build().get().uri(cartServiceUrl + "/cart").retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToFlux(CartItem.class);
    }

    public Mono<CartItem> getCartItem(Integer cartItemId) {
        return builder.build().get().uri(cartServiceUrl + "/cart/{cartItemId}", cartItemId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(CartItem.class);
    }

    public Mono<CartItem> addCartItem(CartItemField field) {
        return builder.build().post().uri(cartServiceUrl + "/cart")
            .contentType(MediaType.APPLICATION_JSON).body(Mono.just(field), CartItem.class).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(CartItem.class);
    }

    public Mono<CartItem> updateCartItem(Integer cartItemId, Integer num) {
        return builder.build().put().uri(cartServiceUrl + "/cart/{cartItemId}", cartItemId)
            .contentType(MediaType.APPLICATION_JSON).body(Mono.just(num), Integer.class).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(CartItem.class);
    }

    public Mono<Void> deleteCartItem(Integer cartItemId) {
        return builder.build().delete().uri(cartServiceUrl + "/cart/{cartItemId}", cartItemId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(Void.class);
    }

    public Flux<Order> buy() {
        return builder.build().post().uri(orderServiceUrl + "/orders").contentType(MediaType.APPLICATION_JSON)
            .body(builder.build().get().uri(cartServiceUrl + "/cart").retrieve().bodyToFlux(CartItem.class)
                    .map(cartItem -> new OrderField(cartItem.getProductId(), cartItem.getNum())), OrderField.class)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToFlux(Order.class)
            .doFinally(none -> {
                builder.build().delete().uri(cartServiceUrl + "/cart").retrieve().bodyToMono(Void.class)
                        .subscribe();
            });
    }

    public Flux<Order> getOrders() {
        return builder.build().get().uri(orderServiceUrl + "/orders").retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToFlux(Order.class);
    }

    public Mono<Order> getOrder(Integer orderId) {
        return builder.build().post().uri(orderServiceUrl + "/orders/{orderId}", orderId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(Order.class);
    }

    public Flux<Delivery> getDeliveries() {
        return builder.build().get().uri(deliveryServiceUrl + "/deliveries").retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToFlux(Delivery.class);
    }

    public Mono<Delivery> getDelivery(Integer deliveryId) {
        return builder.build().get().uri(deliveryServiceUrl + "/deliveries/{deliveryId}", deliveryId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToMono(Delivery.class);
    }

    public Flux<Delivery> getDeliveryFromOrderId(Integer orderId) {
        return builder.build().get().uri(deliveryServiceUrl + "/deliveries?orderId={orderId}", orderId).retrieve()
            .onStatus(status -> status.is4xxClientError(), 
				error -> Mono.error(new WebPosRequestException(HttpStatus.NOT_FOUND, "resource not found")))
            .onStatus(status -> status.is5xxServerError(), 
                error -> Mono.error(new WebPosRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")))
            .bodyToFlux(Delivery.class);
    }

}
