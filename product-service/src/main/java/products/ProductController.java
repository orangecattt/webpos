package products;

import jakarta.servlet.http.HttpServletRequest;
import products.model.Product;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    HttpServletRequest req;

    final String picPath = "/products-images/";

    @GetMapping("")
    Mono<ResponseEntity<List<Product>>> getProducts(
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "64") Integer size) 
    {
        System.out.println("get-products");
        return productService.getProducts(page, size)
            .collectList()
            .map(products -> ResponseEntity.ok(products));
    }

    @GetMapping("/{productId}")
    Mono<ResponseEntity<Product>> getProduct(@PathVariable("productId") String productId) {
        return productService.getProduct(productId)
            .map(product -> ResponseEntity.ok(product))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}