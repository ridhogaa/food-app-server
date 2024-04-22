package org.ergea.foodapp.mapper;

import org.ergea.foodapp.dto.OrderDetailResponse;
import org.ergea.foodapp.dto.OrderResponse;
import org.ergea.foodapp.entity.Order;
import org.ergea.foodapp.entity.OrderDetail;

public final class OrderMapper {
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
