package com.increff.pos.controller;

import com.increff.pos.dto.CartDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api

public class CartController {

    @Autowired
    private CartDto cartDto;

    @ApiOperation(value="Adding an item to the cart")
    @RequestMapping(path="/api/cart", method = RequestMethod.POST)
    public void add(@RequestBody CartForm f)throws ApiException{
            cartDto.add(f);
    }

    @ApiOperation(value="Removing an item from the cart")
    @RequestMapping(path="/api/cart/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        cartDto.delete(id);
    }

    @ApiOperation(value="Getting details of a particular item in the cart")
    @RequestMapping(path="/api/cart/{id}", method = RequestMethod.GET)
    public CartData get(@PathVariable int id) throws ApiException {
        return cartDto.get(id);
    }

    @ApiOperation(value="Updating details of a particular item in the cart")
    @RequestMapping(path="/api/cart/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody CartForm f) throws ApiException {
        cartDto.update(id,f);
    }

    @ApiOperation(value="Getting details of all the items in the cart")
    @RequestMapping(path="/api/cart", method = RequestMethod.GET)
    public List<CartData> getAll(){
        return cartDto.getAll();
    }

    @ApiOperation(value = "Deleting all items from the current order")
    @RequestMapping(path = "/api/cartFlush", method = RequestMethod.DELETE)
    public void deleteAll() throws ApiException {
        cartDto.flushAll();
    }

    @ApiOperation(value = "Creating a new order and pushing all items into an order")
    @RequestMapping(path = "/api/cartPushToOrder", method = RequestMethod.POST)
    public void pushToNewOrder(@RequestBody OrderForm f) throws ApiException {
        cartDto.pushToNewOrder(f);
    }

}