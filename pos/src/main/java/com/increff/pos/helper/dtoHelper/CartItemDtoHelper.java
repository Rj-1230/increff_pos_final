package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.form.CartItemForm;
import com.increff.pos.pojo.CartItemPojo;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service

public class CartItemDtoHelper {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    public static CartItemPojo convert(CartItemForm f) {
        CartItemPojo c = new CartItemPojo();
        c.setQuantity(f.getQuantity());
        c.setSellingPrice(f.getSellingPrice());
        c.setCounterId(getPrincipal().getId());
        return c;
    }

    public static CartItemData convert(CartItemPojo p) {
        CartItemData d = new CartItemData();
        d.setProductId(p.getProductId());
        d.setCartItemId(p.getCartItemId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setCounterId(p.getCounterId());
        return d;
    }


    public static void normalize(CartItemForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        f.setSellingPrice(Double.parseDouble(formatter.format(f.getSellingPrice())));

        if (f.getBarcode().length() > 20) {
            f.setBarcode(f.getBarcode().substring(0, 20));
        }
    }
}
