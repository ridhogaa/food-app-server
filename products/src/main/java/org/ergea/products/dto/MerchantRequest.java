package org.ergea.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    @NotBlank(message = "Must not empty")
    private String name;
    @NotBlank(message = "Must not empty")
    private String location;
    @NotNull(message = "must not be null")
    private Boolean isOpen;
}
