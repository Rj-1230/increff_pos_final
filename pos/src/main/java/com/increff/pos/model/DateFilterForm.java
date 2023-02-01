package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PastOrPresent;

@Getter
@Setter
public class DateFilterForm {
    @PastOrPresent
    private String start;
    @PastOrPresent
    private String end;
}