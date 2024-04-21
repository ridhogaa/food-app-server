package org.ergea.foodapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantRequest {
    @NotBlank(message = "Must not empty")
    private String name;
    @NotBlank(message = "Must not empty")
    private String location;
    @NotNull(message = "must not be null")
    private Boolean isOpen;
}
