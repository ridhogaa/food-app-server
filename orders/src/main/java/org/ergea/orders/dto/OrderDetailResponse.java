package org.ergea.orders.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResponse {
    private UUID id;
    private Integer quantity;
    private Double totalPrice;
}
