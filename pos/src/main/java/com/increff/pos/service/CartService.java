package com.increff.pos.service;

import com.increff.pos.dao.CartDao;
import com.increff.pos.model.CartData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class CartService {
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
    public List<CartPojo> getAll() {
        Integer id = getPrincipal().getId();
        return cartDao.selectAll(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void deleteAll() throws ApiException{
        List<CartPojo> cartPojoList = getAll();
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