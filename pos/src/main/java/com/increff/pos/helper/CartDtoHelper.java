package com.increff.pos.helper;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service

public class CartDtoHelper {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    public static List<CartData> getAllCartItems(List<CartPojo> list){
        List<CartData> list2 = new ArrayList<CartData>();
        for(CartPojo p: list){
            if(p.getQuantity()==0){
                continue;
            }
            list2.add(convert(p));
        }
        return list2;
    }


    public static CartPojo convert(CartForm f){
        CartPojo c = new CartPojo();
        c.setQuantity(f.getQuantity());
        c.setSellingPrice(f.getSellingPrice());
        c.setCounterId(getPrincipal().getId());
        return c;
    }

    public static CartData convert(CartPojo p){
        CartData d = new CartData();
        d.setProductId(p.getProductId());
        d.setCartItemId(p.getCartItemId());
        d.setProductName(p.getProductName());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setCounterId(p.getCounterId());
        return d;
    }


    public static void normalize(CartForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        f.setSellingPrice(Double.parseDouble(formatter.format(f.getSellingPrice())));

        if(f.getBarcode().length()>15){
            f.setBarcode(f.getBarcode().substring(0,15));
        }
    }
}
