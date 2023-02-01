package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CartForm {
    @NotNull
    @Size(min=1, max=15)
    private String barcode;
    @NotNull
    @Min(1)
    @Max(99)
    private Integer quantity;
    @NotNull
    @Min(18)
    private Double sellingPrice;

}
