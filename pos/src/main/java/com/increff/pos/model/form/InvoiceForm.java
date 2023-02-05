package com.increff.pos.model.form;

import com.increff.pos.model.data.OrderItemData;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    @NotNull
    private Integer orderId;
    @NotBlank
    @Size(min=1, max=20)
    private String customerName;
    @NotBlank
    @Size(min=10,max=10)
    private String customerPhone;
    @NotBlank
    private String invoiceTime;
    @NotEmpty
    private List<OrderItemData> orderItemList;
}