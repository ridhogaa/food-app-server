package org.ergea.foodapp.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailRequest {
    @NotNull(message = "Must not empty")
    private Integer quantity;
    @NotNull(message = "Must not empty")
    private Double totalPrice;
    @NotNull(message = "Must not empty")
    private String orderId;
    @NotNull(message = "Must not empty")
    private String productId;
}
