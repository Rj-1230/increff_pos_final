package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter

public class ProductForm {
    @NotBlank
    @Size(min=1,max=20)
    private String barcode;
    @NotBlank
    @Size(min=1,max=20)
    private String brandName;
    @NotBlank
    @Size(min=1,max=20)
    private String categoryName;
    @NotBlank
    @Size(min=1,max=20)
    private String name;
    @NotNull
    @Min(0)
    @Max(999999)
    private Double mrp;
}
