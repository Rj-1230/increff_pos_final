package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerDetailsForm {
    @NotBlank
    @Size(min = 1, max = 20)
    private String customerName;
    @NotBlank
    @Size(min = 10, max = 10)
    private String customerPhone;

}
