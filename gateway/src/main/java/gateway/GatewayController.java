package gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.RequestContextFilter;
import gateway.GatewayService.WebPosRequestException;
import gateway.model.CartItem;
import gateway.model.CartItemField;
import gateway.model.Delivery;
import gateway.model.Order;
import gateway.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    RequestContextFilter req;
    
    @GetMapping("/products")
    public Mono<ResponseEntity<CollectionModel<EntityModel<Product>>>> getProducts(
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "64") Integer size
    ){
        return gatewayService.getProducts(page, size)
            .map(product -> toResourceProduct(product))
            .collectList()
            .map(products -> toCollectionResourceProduct(products, page, size))
            .map(collectionResource -> ResponseEntity.ok(collectionResource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/products/{productId}")
    public Mono<ResponseEntity<EntityModel<Product>>> getProduct(@PathVariable("productId") String productId){
        Mono<Product> monoProduct = gatewayService.getProduct(productId);
        return monoProduct.map(product -> toResourceProduct(product))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/cart")
    public Mono<ResponseEntity<CollectionModel<EntityModel<CartItem>>>> getCart(){
        return gatewayService.getCart()
            .map(cartItem -> toResourceCartItem(cartItem))
            .collectList()
            .map(cartItems -> toCollectionResourceCartItem(cartItems))
            .map(collectionResource -> ResponseEntity.ok(collectionResource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<EntityModel<CartItem>>> getCartItem(@PathVariable Integer cartItemId){
        return gatewayService.getCartItem(cartItemId)
            .map(cartItem -> toResourceCartItem(cartItem))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorReturn(ResponseEntity.notFound().build())
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @PostMapping("/cart")
    public Mono<ResponseEntity<EntityModel<CartItem>>> addCartItem(@RequestBody CartItemField field){
        return gatewayService.addCartItem(field)
            .map(cartItem -> toResourceCartItem(cartItem))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @PutMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<EntityModel<CartItem>>> updateCartItem(@PathVariable Integer cartItemId, @RequestBody Integer num){
        return gatewayService.updateCartItem(cartItemId, num)
            .map(cartItem -> toResourceCartItem(cartItem))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<Object>> deleteCartItem(@PathVariable Integer cartItemId){
        return gatewayService.deleteCartItem(cartItemId)
            .flatMap(none -> Mono.just(ResponseEntity.noContent().build()))
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @PutMapping("/buy")
    public Mono<ResponseEntity<CollectionModel<EntityModel<Order>>>> buy(){
        return gatewayService.buy()
            .map(order -> toResourceOrder(order))
            .collectList()
            .map(orders -> toCollectionResourceOrder(orders))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/orders")
    public Mono<ResponseEntity<CollectionModel<EntityModel<Order>>>> getOrders(){
        return gatewayService.getOrders()
            .map(order -> toResourceOrder(order))
            .collectList()
            .map(orders -> toCollectionResourceOrder(orders))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/orders/{orderId}")
    public Mono<ResponseEntity<EntityModel<Order>>> getOrder(@PathVariable Integer orderId){
        return gatewayService.getOrder(orderId)
            .map(order -> toResourceOrder(order))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/deliveries")
    public Mono<ResponseEntity<CollectionModel<EntityModel<Delivery>>>> getDeliveries(
        @RequestParam(name = "orderId", required = false) Integer orderId
    ){
        Flux<Delivery> deliveryFlux;
        if (orderId == null){
            deliveryFlux = gatewayService.getDeliveries();        
        }
        else{
            deliveryFlux = gatewayService.getDeliveryFromOrderId(orderId);
        }
        return deliveryFlux
            .map(delivery -> toResourceDelivery(delivery))
            .collectList()
            .map(deliveries -> toCollectionResourceDelivery(deliveries))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    @GetMapping("/deliveries/{deliveryId}")
    public Mono<ResponseEntity<EntityModel<Delivery>>> getDelivery(@PathVariable Integer deliveryId){
        return gatewayService.getDelivery(deliveryId)
            .map(delivery -> toResourceDelivery(delivery))
            .map(resource -> ResponseEntity.ok(resource))
            .onErrorResume(WebPosRequestException.class, e -> Mono.just(ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getCode(), e.getMsg())).build()))
            .onErrorReturn(ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "server is not responding")).build());
    }

    private EntityModel<Product> toResourceProduct(Product product){
        return EntityModel.of(product);
    }

    private CollectionModel<EntityModel<Product>> toCollectionResourceProduct(List<EntityModel<Product>> products, int page, int size){
        CollectionModel<EntityModel<Product>> res = CollectionModel.of(products);
        if (page > 0) res.add(Link.of(String.format("http://localhost:8080/products?page=%d&size=%d", page-1, size), "last page"));
        res.add(Link.of(String.format("http://localhost:8080/products?page=%d&size=%d", page+1, size), "next page"));
        return res;
    }

    private EntityModel<CartItem> toResourceCartItem(CartItem cartItem){
        return EntityModel.of(cartItem);
    }

    private CollectionModel<EntityModel<CartItem>> toCollectionResourceCartItem(List<EntityModel<CartItem>> cartItems){
        return CollectionModel.of(cartItems);
    }

    private EntityModel<Order> toResourceOrder(Order order){
        return EntityModel.of(order);
    }

    private CollectionModel<EntityModel<Order>> toCollectionResourceOrder(List<EntityModel<Order>> orders){
        return CollectionModel.of(orders);
    }

    private EntityModel<Delivery> toResourceDelivery(Delivery delivery){
        return EntityModel.of(delivery);
    }

    private CollectionModel<EntityModel<Delivery>> toCollectionResourceDelivery(List<EntityModel<Delivery>> deliveries){
        return CollectionModel.of(deliveries);
    }

}
