package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class CartForm {
    @NotBlank
    @Size(min=1, max=15)
    private String barcode;
    @NotNull
    @Positive
    @Min(1)
    @Max(99)
    private Integer quantity;
    @NotNull
    @Min(0)
    @Max(999999)
    private Double sellingPrice;

}
