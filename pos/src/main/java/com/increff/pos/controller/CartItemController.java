package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.CartItemDto;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.form.CartItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api

public class CartItemController {

    @Autowired
    private CartItemDto cartItemDto;

    @ApiOperation(value = "Adding an item to the cart")
    @RequestMapping(path = "/api/cart", method = RequestMethod.POST)
    public void add(@RequestBody CartItemForm cartItemForm) throws ApiException {
        cartItemDto.addCartItem(cartItemForm);
    }

    @ApiOperation(value = "Removing an item from the cart")
    @RequestMapping(path = "/api/cart/{cartItemId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer cartItemId) throws ApiException {
        cartItemDto.deleteCartItem(cartItemId);
    }

    @ApiOperation(value = "Getting details of a particular item in the cart")
    @RequestMapping(path = "/api/cart/{cartItemId}", method = RequestMethod.GET)
    public CartItemData get(@PathVariable Integer cartItemId) throws ApiException {
        return cartItemDto.getCartItem(cartItemId);
    }

    @ApiOperation(value = "Updating details of a particular item in the cart")
    @RequestMapping(path = "/api/cart/{cartItemId}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer cartItemId, @RequestBody CartItemForm cartItemForm) throws ApiException {
        cartItemDto.updateCartItem(cartItemId, cartItemForm);
    }

    @ApiOperation(value = "Getting details of all the items in the cart")
    @RequestMapping(path = "/api/cart", method = RequestMethod.GET)
    public List<CartItemData> getAll() throws ApiException {
        return cartItemDto.getAll();
    }

    @ApiOperation(value = "Deleting all items from the current order")
    @RequestMapping(path = "/api/cartFlush", method = RequestMethod.DELETE)
    public void deleteAllCartItems() throws ApiException {
        cartItemDto.flushCartItems();
    }


}