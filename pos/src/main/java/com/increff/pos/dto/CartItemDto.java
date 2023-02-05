package com.increff.pos.dto;

import com.increff.pos.flow.CartItemFlow;

import com.increff.pos.api.*;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.form.CartItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.*;
import static com.increff.pos.util.NullCheckHelper.*;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class CartItemDto {
    @Autowired
    private CartItemApi cartItemApi;
    @Autowired
    private CartItemFlow cartItemFlow;

    public void addCartItem(CartItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartItemFlow.add(convert(f),f.getBarcode());
    }

    public void deleteCartItem(Integer id) throws ApiException{
        cartItemApi.delete(id);
    }

    public CartItemData getCartItem(Integer id) throws ApiException {
        return convert(cartItemApi.getCheck(id));
    }

    public void updateCartItem(@PathVariable Integer id, @RequestBody CartItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        cartItemFlow.update(id,convert(f));
    }
    public List<CartItemData> getAll() throws ApiException{
        return cartItemFlow.getAllCartItemsOfCounterId();
//                getAllCartItems(cartItemApi.getAll(getPrincipal().getId()));
    }

    public void flushCartItems()throws ApiException{
        cartItemApi.deleteAll(cartItemApi.getAllItemsInCart(getPrincipal().getId()));
    }
}