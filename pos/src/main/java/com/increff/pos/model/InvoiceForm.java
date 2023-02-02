package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    @NotNull
    private Integer orderId;
    @NotBlank
    @Size(min=1, max=15)
    private String customerName;
    @NotBlank
    @Size(min=10,max=10)
    private String customerPhone;
    @NotBlank
    private String invoiceTime;
    @NotEmpty
    private List<OrderItemData> orderItemList;
}