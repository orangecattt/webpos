package delivery.model;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Delivery extends DeliveryField {
    
    public enum DeliveryStatus{
        NOT_EXIST, CREATED
    }

    @Id
    private Integer deliveryId;

    private DeliveryStatus status;

    public Delivery(DeliveryField field){
        status = DeliveryStatus.CREATED;
        orderId = field.orderId;
    }

}
