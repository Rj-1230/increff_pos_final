package com.increff.pos.flow;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.CartItemApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.pojo.CartItemPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.convert;
import static com.increff.pos.helper.flowHelper.CartItemFlowHelper.checkMrpAndInventoryForCartPojo;
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
        ProductPojo productPojo = productApi.getCheckProduct(barcode);
        cartItemPojo.setProductId(productPojo.getProductId());
        InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(cartItemPojo.getProductId());
        Integer inventoryQuantity = checkMrpAndInventoryForCartPojo(cartItemPojo, productPojo, inventoryPojo);
        cartItemApi.add(cartItemPojo, inventoryQuantity);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, CartItemPojo newCartItemPojo) throws ApiException {
        CartItemPojo exCartItemPojo = cartItemApi.getCheck(id);
        ProductPojo productPojo = productApi.getCheckProduct(exCartItemPojo.getProductId());
        InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(exCartItemPojo.getProductId());
        checkMrpAndInventoryForCartPojo(newCartItemPojo, productPojo, inventoryPojo);
        cartItemApi.update(exCartItemPojo, newCartItemPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public CartItemData getCartItem(Integer cartItemId) throws ApiException {
        CartItemPojo cartItemPojo = cartItemApi.getCheck(cartItemId);
        CartItemData cartItemData = convert(cartItemApi.getCheck(cartItemId));
        cartItemData.setBarcode(productApi.getCheckProduct(cartItemPojo.getProductId()).getBarcode());
        cartItemData.setProductName(productApi.getCheckProduct(cartItemPojo.getProductId()).getName());
        return cartItemData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<CartItemData> getAllCartItemsOfCounterId() throws ApiException {
        List<CartItemPojo> cartItemPojoList = cartItemApi.getAllItemsInCart(getPrincipal().getId());
        List<CartItemData> list2 = new ArrayList<CartItemData>();
        for (CartItemPojo cartItemPojo : cartItemPojoList) {
            if (cartItemPojo.getQuantity() == 0)
                continue;
            String productName = productApi.getCheckProduct(cartItemPojo.getProductId()).getName();
            String barcode = productApi.getCheckProduct(cartItemPojo.getProductId()).getBarcode();
            list2.add(convertCartPojoToCartData(cartItemPojo, productName, barcode));
        }
        return list2;
    }

}
