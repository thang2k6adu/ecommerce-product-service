package com.ecommerce.productservice.modules.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to merge a guest cart with the authenticated user's cart")
public class MergeCartRequest {

    @Schema(description = "Session ID of the guest cart to merge", example = "sess-12345", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Guest session ID is required")
    private String guestSessionId;
}
