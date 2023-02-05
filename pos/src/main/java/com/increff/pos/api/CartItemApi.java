package com.increff.pos.api;

import com.increff.pos.dao.CartItemDao;
import com.increff.pos.pojo.CartItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class CartItemApi {
    @Autowired
    private CartItemDao cartItemDao;

    public void add(CartItemPojo cartItemPojo, Integer quantity) throws ApiException {
        CartItemPojo exCartItemPojo = cartItemDao.getCartPojoFromProductIdAndCounterId(cartItemPojo.getProductId(), cartItemPojo.getCounterId());
        if(Objects.nonNull(exCartItemPojo)){
            if(exCartItemPojo.getQuantity()+ cartItemPojo.getQuantity()>quantity){
                throw new ApiException("Item can't be added to cart as it exceeds the inventory. Items already in cart : "+ exCartItemPojo.getQuantity() +" Present inventory count :"+quantity);
            }
            exCartItemPojo.setQuantity(exCartItemPojo.getQuantity()+ cartItemPojo.getQuantity());
            exCartItemPojo.setSellingPrice(cartItemPojo.getSellingPrice());
        }
        else{
            cartItemDao.insert(cartItemPojo);
        }
    }
    public void delete(int id) throws ApiException {
        getCheck(id);
        cartItemDao.delete(id);
    }

    public List<CartItemPojo> getAllItemsInCart(Integer id) {
        return cartItemDao.selectAllCartPojoByCounterId(id);
    }

    public void deleteAll(List<CartItemPojo> cartItemPojoList) throws ApiException{
        for(CartItemPojo cartItemPojo : cartItemPojoList){
            delete(cartItemPojo.getCartItemId());
        }
    }

    public void update(CartItemPojo exCartItemPojo, CartItemPojo newCartItemPojo) throws ApiException {
        exCartItemPojo.setSellingPrice(newCartItemPojo.getSellingPrice());
        exCartItemPojo.setQuantity(newCartItemPojo.getQuantity());
    }

    public CartItemPojo getCheck(Integer id) throws ApiException {
            CartItemPojo cartItemPojo = cartItemDao.select(id);
            if(!Objects.nonNull(cartItemPojo)){
                throw new ApiException("No such item exists in cart with given Id");
            }
            return cartItemPojo;
    }

}