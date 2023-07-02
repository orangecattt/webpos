package cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cart.model.CartItem;
import cart.model.CartItemField;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public Mono<CartItem> addCart(CartItemField field) {
        CartItem cart = new CartItem();
        cart.setField(field);
        return cartItemRepository.save(cart);
    }

    public Mono<CartItem> updateCart(Integer id, int num) {
        return cartItemRepository.findById(id)
                .flatMap(cartItem -> {
                    cartItem.setNum(num);
                    return cartItemRepository.save(cartItem);
                });
    }

    public Mono<Void> removeCarts(){
        return cartItemRepository.deleteAll();
    }

    public Mono<Boolean> removeCart(Integer id) {
        return cartItemRepository.existsById(id)
                .doOnNext(
                        isExist -> {
                            if (isExist) {
                                cartItemRepository.deleteById(id).subscribe();
                            }
                        });
    }

    public Flux<CartItem> getCarts() {
        return cartItemRepository.findAll();
    }

    public Mono<CartItem> getCart(Integer id) {
        return cartItemRepository.findById(id)
                .onErrorResume(e -> Mono.empty());
    }

}
