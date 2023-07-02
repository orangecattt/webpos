package gateway.model;

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

}