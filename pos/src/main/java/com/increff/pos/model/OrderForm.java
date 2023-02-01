package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OrderForm {
    @NotNull
    @Size(min = 1, max = 15)
    private String customerName;
    @NotNull
    @Size(min = 10, max = 10)
    private String customerPhone;

}
