package com.increff.pos.dto;

import com.increff.pos.flow.CartFlow;
import com.increff.pos.model.*;
import com.increff.pos.pojo.CartPojo;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.CartDtoHelper.*;
import static com.increff.pos.helper.OrderDtoHelper.*;
import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class CartDto {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartFlow cartFlow;

    public void add(CartForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartFlow.add(convert(f),f.getBarcode());
    }

    public void delete(Integer id) throws ApiException{
        cartService.delete(id);
    }

    public CartData get(Integer id) throws ApiException {
        return convert(cartService.getCheck(id));
    }

    public void update(@PathVariable Integer id, @RequestBody CartForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartFlow.update(id,convert(f));
    }
    public List<CartData> getAll(){
        return getAllCartItems(cartService.getAll());
    }

    public void flushAll()throws ApiException{
        cartService.deleteAll();
    }
}