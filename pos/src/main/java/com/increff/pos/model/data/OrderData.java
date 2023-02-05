package com.increff.pos.model.data;
import com.increff.pos.model.form.CustomerDetailsForm;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData extends CustomerDetailsForm {
   private Integer orderId;
   private ZonedDateTime orderCreateTime;
    private ZonedDateTime orderInvoiceTime;
    private String status;
    private Integer counterId;
    private String orderCode;
}
