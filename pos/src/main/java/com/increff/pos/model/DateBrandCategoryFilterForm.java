package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DateBrandCategoryFilterForm {
    @NotBlank
    private String start;
    @NotBlank
    private String end;
    @NotBlank
    private String brand;
    @NotBlank
    private String category;
}
