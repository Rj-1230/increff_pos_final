package com.increff.pos.service;

import com.increff.pos.dao.CartDao;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartServiceTest extends AbstractUnitTest{
    @Autowired
    private CartService cartService;
    @Autowired
    private CartDao cartDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddCartItem() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        Integer inventoryQuantity =  10;
        CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
        cartService.add(cartPojo,inventoryQuantity);

        List<CartPojo> cartPojoList =  cartService.getAllItemsInCart();
        assertEquals(cartPojoList.size(),1);
        assertEquals(cartPojoList.get(0).getProductId(),productId);
        assertEquals(cartPojoList.get(0).getCounterId(),counterId);
        assertEquals(cartPojoList.get(0).getProductName(),productName);
        assertEquals(cartPojoList.get(0).getSellingPrice(),sellingPrice);
        assertEquals(cartPojoList.get(0).getQuantity(),quantity);
    }

    @Test
    public void testSizeOfCartList() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        Integer inventoryQuantity =  10;
        for(int i=0;i<4;i++){
            CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
            cartService.add(cartPojo,inventoryQuantity);
        }
        List<CartPojo> cartPojoList =  cartService.getAllItemsInCart();
        assertEquals(cartPojoList.size(),1);

        for(int i=0;i<4;i++){
            CartPojo cartPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
            cartService.add(cartPojo,inventoryQuantity);
        }
        cartPojoList =  cartService.getAllItemsInCart();
        assertEquals(cartPojoList.size(),4);
    }

    @Test
    public void testDeleteCartItem() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        for(int i=0;i<4;i++){
            CartPojo cartPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
            cartDao.insert(cartPojo);
        }
        List<CartPojo> cartPojoList =  cartService.getAllItemsInCart();
        assertEquals(cartPojoList.size(),4);
        cartService.delete(cartPojoList.get(0).getCartItemId());
        List<CartPojo> newCartPojoList =  cartService.getAllItemsInCart();
        assertEquals(newCartPojoList.size(),3);

//        cartService.deleteAll();
//        assertEquals(cartPojoList.size(),0);
    }

    @Test
    public void updateCartItem() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
        cartDao.insert(cartPojo);

        Double newSellingPrice = 130.62;
        Integer newQuantity =4;
        CartPojo updatedCartPojo = PojoUtil.getCartPojo(productId,counterId,productName,newSellingPrice,newQuantity);
        cartService.update(cartPojo, updatedCartPojo);
        CartPojo resultCartPojo =  cartService.getCheck(cartPojo.getCartItemId());
        assertEquals(resultCartPojo.getProductId(),productId);
        assertEquals(resultCartPojo.getCounterId(),counterId);
        assertEquals(resultCartPojo.getProductName(),productName);
        assertEquals(resultCartPojo.getSellingPrice(),newSellingPrice);
        assertEquals(resultCartPojo.getQuantity(),newQuantity);
    }

    @Test
    public void testGetCheckCartItem() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
        cartDao.insert(cartPojo);

        CartPojo cartPojo1 = cartService.getCheck(cartPojo.getCartItemId());
       assertEquals(cartPojo1,cartPojo);
    }

    @Test
    public void testCartQuantityWithInventoryQuantity() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
        cartDao.insert(cartPojo);

        Integer inventoryQuantity = 3;
        CartPojo newCartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity+1);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Item can't be added to cart as it exceeds the inventory. Items already in cart : "+ cartPojo.getQuantity() +" Present inventory count :"+inventoryQuantity);
        cartService.add(newCartPojo,inventoryQuantity);
    }

    @Test
    public void testProductExistenceInCart() throws ApiException {
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        CartPojo cartPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
        cartDao.insert(cartPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such item exists in cart with given Id");
        cartService.getCheck(cartPojo.getCartItemId()+1);
    }

}
