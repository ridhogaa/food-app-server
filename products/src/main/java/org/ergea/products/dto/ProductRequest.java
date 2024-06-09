package org.ergea.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Must not empty")
    private String name;
    @NotNull(message = "Must not empty")
    private Double price;
    @NotNull(message = "Must not empty")
    private String merchantId;
}
