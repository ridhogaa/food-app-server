package org.ergea.orders.mapper;

import org.ergea.orders.dto.OrderDetailResponse;
import org.ergea.orders.dto.OrderResponse;
import org.ergea.orders.entity.Order;
import org.ergea.orders.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .isComplete(order.getIsComplete())
                .build();
    }

    public OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }
}
