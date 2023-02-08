package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Service
public class OrderItemDtoHelper {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    public static OrderItemPojo convert(OrderItemForm f) {
        OrderItemPojo o = new OrderItemPojo();
        o.setOrderId(f.getOrderId());
        o.setQuantity(f.getQuantity());
        o.setSellingPrice(f.getSellingPrice());
        return o;
    }

    public static void normalize(OrderItemForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        f.setSellingPrice(Double.parseDouble(formatter.format(f.getSellingPrice())));

        if (f.getBarcode().length() > 20) {
            f.setBarcode(f.getBarcode().substring(0, 20));
        }
    }

    public static OrderItemData convert(OrderItemPojo p) {
        OrderItemData d = new OrderItemData();
        d.setOrderItemId(p.getOrderItemId());
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        return d;
    }
}
