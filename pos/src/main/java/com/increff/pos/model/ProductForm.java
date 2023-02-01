package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter

public class ProductForm {
    @NotNull
    @Size(min=1,max=15)
    private String barcode;
    @NotNull
    @Size(min=1,max=15)
    private String brandName;
    @NotNull
    @Size(min=1,max=15)
    private String categoryName;
    @NotNull
    @Size(min=1,max=15)
    private String name;
    @NotNull
    private Double mrp;
}
