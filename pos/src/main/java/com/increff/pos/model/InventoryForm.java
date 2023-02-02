package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    @Size(min=1, max=15)
   private String barcode;
    @NotNull
    @Positive
    @Min(1)
    @Max(99)
    private Integer quantity;



}
