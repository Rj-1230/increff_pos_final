package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    @Size(min = 1, max = 20)
    private String barcode;
    @NotNull
    @Positive
    @Min(1)
    @Max(99)
    private Integer quantity;


}
