package cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cart.model.CartItem;
import cart.model.CartItemField;
import reactor.core.publisher.Mono;

@Controller
public class CartController {
    
    @Autowired
    private CartService cartService;

    @PostMapping("/cart")
    public Mono<ResponseEntity<CartItem>> addCart(@RequestBody CartItemField field){
        return cartService.addCart(field)
            .map(cartItem -> ResponseEntity.ok(cartItem));
    }

    @GetMapping("/cart")
    public Mono<ResponseEntity<List<CartItem>>> getCarts(){
        return cartService.getCarts()
            .collectList()
            .map(cartItems -> ResponseEntity.ok(cartItems));
    }

    @GetMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<CartItem>> getCart(@PathVariable("cartItemId") Integer id){
        return cartService.getCart(id)
            .map(cartItem -> ResponseEntity.ok(cartItem))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<CartItem>> updateCart(@PathVariable("cartItemId") Integer id, @RequestBody Integer num){
        return cartService.updateCart(id, num)
            .map(cartItem -> ResponseEntity.ok(cartItem))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cart")
    public Mono<ResponseEntity<Object>> deleteCarts(){
        return cartService.removeCarts()
            .flatMap(none -> Mono.just(ResponseEntity.noContent().build()))
            .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Mono<ResponseEntity<Object>> deleteCart(@PathVariable("cartItemId") Integer id){
        return cartService.removeCart(id)
            .flatMap(isExist -> {
                if (isExist){
                    return Mono.just(ResponseEntity.noContent().build());
                }
                else{
                    return Mono.just(ResponseEntity.notFound().build());
                }
            }
        );
    }

    // @PutMapping("/cart/buy")
    // public ResponseEntity<Order[]> buy(){
    //     Order[] orders = cartService.buy();
    //     return ResponseEntity.ok(orders);
    // }

}
