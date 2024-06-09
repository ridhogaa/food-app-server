package org.ergea.orders.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Must not empty")
    private String destinationAddress;
    @NotNull(message = "must not be null")
    private Boolean isComplete;
}
