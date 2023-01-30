package com.increff.pos.helper;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.GetCurrentTime.getCurrentDateTime;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class OrderDtoHelper {

    public static OrderPojo convert(OrderForm f){
        OrderPojo p = new OrderPojo();
        p.setCustomerPhone(f.getCustomerPhone());
        p.setCustomerName(f.getCustomerName());
        p.setOrderCreateTime(ZonedDateTime.now());
        p.setCounterId(getPrincipal().getId());
        p.setStatus("created");
//        p.setOrderInvoiceTime(ZonedDateTime.now());
        return p;
    }

    public static OrderData convert(OrderPojo p){
        OrderData d = new OrderData();
        d.setOrderId(p.getOrderId());
        d.setStatus(p.getStatus());
        d.setCounterId(p.getCounterId());
        d.setOrderCreateTime(p.getOrderCreateTime());
        d.setOrderInvoiceTime(p.getOrderInvoiceTime());
        d.setCustomerName(p.getCustomerName());
        d.setCustomerPhone(p.getCustomerPhone());
        d.setOrderCode(p.getOrderCode());
        return d;
    }

    public static void normalize(OrderForm f) {
        f.setCustomerName(f.getCustomerName().toLowerCase().trim());
        if(f.getCustomerName().length()>15){
            f.setCustomerName(f.getCustomerName().substring(0,15));
        }
        if(f.getCustomerPhone().length()>10){
            f.setCustomerPhone(f.getCustomerPhone().substring(0,10));
        }
    }

    public static List<OrderData> getAllOrders(List<OrderPojo> list){
        List<OrderData> list2 = new ArrayList<OrderData>();
        for(OrderPojo p: list){
            list2.add(convert(p));
        }
        return list2;
    }
}
