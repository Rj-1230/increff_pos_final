package com.increff.pos.dto;

import com.increff.pos.flow.CartFlow;
import com.increff.pos.model.*;

import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.CartDtoHelper.*;
import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class CartDto {
    @Autowired
    private CartApi cartApi;
    @Autowired
    private CartFlow cartFlow;

    public void add(CartForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartFlow.add(convert(f),f.getBarcode());
    }

    public void delete(Integer id) throws ApiException{
        cartApi.delete(id);
    }

    public CartData get(Integer id) throws ApiException {
        return convert(cartApi.getCheck(id));
    }

    public void update(@PathVariable Integer id, @RequestBody CartForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartFlow.update(id,convert(f));
    }
    public List<CartData> getAll(){
        return getAllCartItems(cartApi.getAll(getPrincipal().getId()));
    }

    public void flushAll()throws ApiException{
        cartApi.deleteAll(cartApi.getAll(getPrincipal().getId()));
    }
}