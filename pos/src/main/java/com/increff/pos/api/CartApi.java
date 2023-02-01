package com.increff.pos.api;

import com.increff.pos.dao.CartDao;
import com.increff.pos.pojo.CartPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class CartApi {
    @Autowired
    private CartDao cartDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartPojo cartPojo,Integer quantity) throws ApiException {
        CartPojo exCartPojo = cartDao.getCartPojoFromProductIdAndCounterId(cartPojo.getProductId(), cartPojo.getCounterId());
        if(Objects.nonNull(exCartPojo)){
            if(exCartPojo.getQuantity()+cartPojo.getQuantity()>quantity){
                throw new ApiException("Item can't be added to cart as it exceeds the inventory. Items already in cart : "+exCartPojo.getQuantity() +" Present inventory count :"+quantity);
            }
            exCartPojo.setQuantity(exCartPojo.getQuantity()+cartPojo.getQuantity());
            exCartPojo.setSellingPrice(cartPojo.getSellingPrice());
        }
        else{
            cartDao.insert(cartPojo);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void delete(int id) throws ApiException {
        getCheck(id);
        cartDao.delete(id);
    }

    @Transactional
    public List<CartPojo> getAllItemsInCart() {
        return cartDao.selectAll();
    }
    @Transactional
    public List<CartPojo> getAll(Integer id) {
        return cartDao.selectAll(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void deleteAll(List<CartPojo> cartPojoList) throws ApiException{
        for(CartPojo cartPojo:cartPojoList){
            delete(cartPojo.getCartItemId());
        }
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(CartPojo exCartPojo, CartPojo newCartPojo) throws ApiException {
        exCartPojo.setSellingPrice(newCartPojo.getSellingPrice());
        exCartPojo.setQuantity(newCartPojo.getQuantity());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public CartPojo getCheck(Integer id) throws ApiException {
            CartPojo cartPojo = cartDao.select(id);
            if(!Objects.nonNull(cartPojo)){
                throw new ApiException("No such item exists in cart with given Id");
            }
            return cartPojo;
    }

}