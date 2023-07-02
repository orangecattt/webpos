package gateway.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemField {
    
    protected Integer productId;

    protected int num;
}
