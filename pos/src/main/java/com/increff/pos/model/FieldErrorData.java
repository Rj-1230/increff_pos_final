package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldErrorData {
    String code;
    String field;
    String message;
}
