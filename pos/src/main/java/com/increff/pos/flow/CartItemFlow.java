package com.increff.pos.flow;

import com.increff.pos.model.data.CartItemData;
import com.increff.pos.pojo.*;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.flowHelper.CartItemFlowHelper.checkMrpAndInventoryForCartPojo;
import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.convert;
import static com.increff.pos.helper.flowHelper.CartItemFlowHelper.convertCartPojoToCartData;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class CartItemFlow {
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private CartItemApi cartItemApi;
    @Autowired
    private ProductApi productApi;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartItemPojo cartItemPojo, String barcode) throws ApiException {
        ProductPojo productPojo= productApi.getCheckProduct(barcode);
        cartItemPojo.setProductId(productPojo.getProductId());
        InventoryPojo inventoryPojo = inventoryApi.getCheck(cartItemPojo.getProductId());
        Integer inventoryQuantity = checkMrpAndInventoryForCartPojo(cartItemPojo,productPojo,inventoryPojo);
        cartItemApi.add(cartItemPojo,inventoryQuantity);
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, CartItemPojo newCartItemPojo) throws ApiException {
        CartItemPojo exCartItemPojo = cartItemApi.getCheck(id);
        ProductPojo productPojo = productApi.getCheckProduct(exCartItemPojo.getProductId());
        InventoryPojo inventoryPojo = inventoryApi.getCheck(newCartItemPojo.getProductId());
        checkMrpAndInventoryForCartPojo(newCartItemPojo,productPojo,inventoryPojo);
        cartItemApi.update(exCartItemPojo, newCartItemPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<CartItemData> getAllCartItemsOfCounterId() throws ApiException {
        List<CartItemPojo> cartItemPojoList = cartItemApi.getAllItemsInCart(getPrincipal().getId());
        List<CartItemData> list2 = new ArrayList<CartItemData>();
        for(CartItemPojo cartItemPojo: cartItemPojoList){
            String productName = productApi.getCheckProduct(cartItemPojo.getProductId()).getName();
            list2.add(convertCartPojoToCartData(cartItemPojo,productName));
        }
        return list2;
    }

}
