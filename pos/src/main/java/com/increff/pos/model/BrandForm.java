package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter

public class BrandForm {
    @NotNull
    @Size(min=1, max=15)
    private String brand;
    @NotNull
    @Size(min=1, max=15)
    private String category;
}
