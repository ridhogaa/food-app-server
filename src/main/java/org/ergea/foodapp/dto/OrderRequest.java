package org.ergea.foodapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Must not empty")
    private String destinationAddress;
    @NotNull(message = "must not be null")
    private Boolean isComplete;
    @NotNull(message = "must not empty")
    private UUID userId;
}
