package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.api.ApiException;

import java.util.List;

public class FormUtil {
    public static BrandForm getBrandForm(String brand, String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static InvoiceForm convertToInvoiceForm(OrderPojo orderPojo, List<OrderItemData> orderItemDataList) throws ApiException
    {
        InvoiceForm invoiceForm = new InvoiceForm();
        invoiceForm.setOrderId(orderPojo.getOrderId());
        invoiceForm.setCustomerName(orderPojo.getCustomerName());
        invoiceForm.setCustomerPhone(orderPojo.getCustomerPhone());
        invoiceForm.setInvoiceTime(orderPojo.getOrderInvoiceTime().toString());
        invoiceForm.setOrderItemList(orderItemDataList);
        return invoiceForm;
    }
}
