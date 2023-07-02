package products.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductTable {
    
    @Id
    @Column("asin")
    private String productId;

    @Column("title")
    private String name;

    @Column("main_cat")
    private String mainCat;

    @Column("category")
    private String category;

    @Column("imageURLHighRes")
    private String imageURLHighRes;

}
