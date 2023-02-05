package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.CustomerDetailsForm;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class OrderDtoHelper {

    public static OrderPojo convert(CustomerDetailsForm f){
        OrderPojo p = new OrderPojo();
        p.setCustomerPhone(f.getCustomerPhone());
        p.setCustomerName(f.getCustomerName());
        p.setOrderCreateTime(ZonedDateTime.now());
        p.setCounterId(getPrincipal().getId());
        p.setStatus("created");

        return p;
    }

    public static OrderData convert(OrderPojo p){
        OrderData d = new OrderData();
        d.setOrderId(p.getOrderId());
        d.setStatus(p.getStatus());
        d.setCounterId(p.getCounterId());
        d.setOrderCreateTime(p.getOrderCreateTime().withZoneSameInstant(ZoneId.of("Asia/Calcutta")));
        d.setOrderInvoiceTime(p.getOrderInvoiceTime());
        d.setCustomerName(p.getCustomerName());
        d.setCustomerPhone(p.getCustomerPhone());
        d.setOrderCode(p.getOrderCode());
        return d;
    }

    public static void normalize(CustomerDetailsForm f) {
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
