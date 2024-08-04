package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.jully.website_selling_technology_backend.entities.OrderStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {
    @JsonProperty("user_id")
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User's ID must be > 0")
    private Long userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Phone number must be valid"
    )
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private float totalMoney;

    @JsonProperty("sub_total")
    @Min(value = 0, message = "Sub total money must be >= 0")
    private float subTotal;

    @JsonProperty("shipping_cost")
    @Min(value = 0, message = "Delivery cost must be >= 0")
    private float shippingCost;

    @JsonProperty("payment_cost")
    @Min(value = 0, message = "Payment cost must be >= 0")
    private float paymentCost;

    @JsonProperty("payment_status")
    private boolean paymentStatus;

    @JsonProperty("shipping_address")
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method_id")
    @NotNull(message = "Payment method ID is required")
    @Min(value = 1, message = "Payment method ID must be > 0")
    private Long paymentMethodId;

    @JsonProperty("shipping_method_id")
    @NotNull(message = "Shipping method ID is required")
    @Min(value = 1, message = "Shipping method ID must be > 0")
    private Long shippingMethodId;

    @JsonProperty("tracking_number")
    @NotNull(message = "Tracking number is required")
    private int trackingNumber;

//    @JsonProperty("cart_items")
//    private List<CartItemDTO> cartItems;
}
