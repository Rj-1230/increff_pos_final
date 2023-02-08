package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DateBrandCategoryFilterForm {
    @NotBlank
    private String start;
    @NotBlank
    private String end;
    @NotBlank
    @Size(min = 1, max = 20)
    private String brand;
    @NotBlank
    @Size(min = 1, max = 20)
    private String category;
}
