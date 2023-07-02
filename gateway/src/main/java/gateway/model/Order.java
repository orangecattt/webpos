package gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends OrderField {

    private Integer orderId;

    public void setField(OrderField orderField){
        this.productId = orderField.productId;
        this.num = orderField.num;
    }

}
