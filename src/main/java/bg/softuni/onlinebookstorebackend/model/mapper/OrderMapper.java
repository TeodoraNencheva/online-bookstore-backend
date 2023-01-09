package bg.softuni.onlinebookstorebackend.model.mapper;

import bg.softuni.onlinebookstorebackend.model.dto.order.OrderListDTO;
import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderListDTO orderEntityToOrderListDTO(OrderEntity order);
}
