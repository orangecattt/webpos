package gateway.model;

import lombok.Data;

@Data
public class CartItem extends CartItemField {
    
    private Integer cartItemId;

    public void setField(CartItemField field){
        productId = field.productId;
        num = field.num;
    }

}
