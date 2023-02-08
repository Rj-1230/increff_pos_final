package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.CartItemApi;
import com.increff.pos.flow.CartItemFlow;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.form.CartItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.convert;
import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.normalize;
import static com.increff.pos.util.SecurityUtil.getPrincipal;
import static com.increff.pos.util.ValidateFormUtil.validateForm;

@Service
public class CartItemDto {
    @Autowired
    private CartItemApi cartItemApi;
    @Autowired
    private CartItemFlow cartItemFlow;

    public void addCartItem(CartItemForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        cartItemFlow.add(convert(f), f.getBarcode());
    }

    public void deleteCartItem(Integer id) throws ApiException {
        cartItemApi.delete(id);
    }

    public CartItemData getCartItem(Integer id) throws ApiException {
        return cartItemFlow.getCartItem(id);
    }

    public void updateCartItem(Integer id, CartItemForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        cartItemFlow.update(id, convert(f));
    }

    public List<CartItemData> getAll() throws ApiException {
        return cartItemFlow.getAllCartItemsOfCounterId();
    }

    public void flushCartItems() throws ApiException {
        cartItemApi.deleteAll(cartItemApi.getAllItemsInCart(getPrincipal().getId()));
    }
}