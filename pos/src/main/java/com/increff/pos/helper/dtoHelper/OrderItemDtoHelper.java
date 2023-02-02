package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemDtoHelper {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    public static OrderItemPojo convert(OrderItemForm f){
        OrderItemPojo o = new OrderItemPojo();
        o.setOrderId(f.getOrderId());
        o.setQuantity(f.getQuantity());
        o.setSellingPrice(f.getSellingPrice());
        return o;
    }

    public static void normalize(OrderItemForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        f.setSellingPrice(Double.parseDouble(formatter.format(f.getSellingPrice())));

        if(f.getBarcode().length()>15){
            f.setBarcode(f.getBarcode().substring(0,15));
        }
    }

    public static OrderItemData convert(OrderItemPojo p){
        OrderItemData d = new OrderItemData();
        d.setOrderItemId(p.getOrderItemId());
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setProductName(p.getProductName());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        return d;
    }


    public static List<OrderItemData> getAllOrderItemsOfAgivenOrder(List<OrderItemPojo> list) {
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo p : list) {
            if(p.getQuantity()==0)
                continue;
            list2.add(convert(p));
        }
        return list2;
    }
}
