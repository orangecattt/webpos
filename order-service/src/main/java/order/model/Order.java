package order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends OrderField {

    @Id
    private Integer orderId;

    public void setField(OrderField orderField){
        this.productId = orderField.productId;
        this.num = orderField.num;
    }

}
