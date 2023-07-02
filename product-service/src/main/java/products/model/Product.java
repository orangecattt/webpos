package products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String productId;

    private String name;

    private String mainCat;

    private String[] category;

    private String[] imageURLHighRes;

    public Product(ProductTable productTable){
        productId = productTable.getProductId();
        name = productTable.getName();
        mainCat = productTable.getMainCat();
        category = productTable.getCategory().split(",");
        imageURLHighRes = productTable.getImageURLHighRes().split(",");
    }
}
