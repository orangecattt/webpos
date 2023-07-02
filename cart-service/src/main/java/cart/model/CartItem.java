package cart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table
public class CartItem extends CartItemField {
    
    @Id
    private Integer cartItemId;

    public void setField(CartItemField field){
        productId = field.productId;
        num = field.num;
    }

}
