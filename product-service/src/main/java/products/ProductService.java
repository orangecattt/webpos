package products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import products.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<Product> getProducts(Integer page, Integer size) {
        return productRepository.findBy(PageRequest.of(page, size)).map(productTable -> new Product(productTable));
    }

    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId).map(productTable -> new Product(productTable));
    }
}