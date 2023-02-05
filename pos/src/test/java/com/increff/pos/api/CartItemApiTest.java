//package com.increff.pos.api;
//
//import com.increff.pos.config.AbstractUnitTest;
//import com.increff.pos.dao.CartItemDao;
//import com.increff.pos.pojo.CartItemPojo;
//import com.increff.pos.util.PojoUtil;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class CartItemApiTest extends AbstractUnitTest {
//    @Autowired
//    private CartItemApi cartItemApi;
//    @Autowired
//    private CartItemDao cartItemDao;
//    @Rule
//    public ExpectedException exceptionRule = ExpectedException.none();
//
//    @Test
//    public void testAddCartItem() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        Integer inventoryQuantity =  10;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,sellingPrice,quantity);
//        cartItemApi.add(cartItemPojo,inventoryQuantity);
//
//        List<CartItemPojo> cartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(cartItemPojoList.size(),1);
//        assertEquals(cartItemPojoList.get(0).getProductId(),productId);
//        assertEquals(cartItemPojoList.get(0).getCounterId(),counterId);
//        assertEquals(cartItemPojoList.get(0).getProductName(),productName);
//        assertEquals(cartItemPojoList.get(0).getSellingPrice(),sellingPrice);
//        assertEquals(cartItemPojoList.get(0).getQuantity(),quantity);
//    }
//
//    @Test
//    public void testSizeOfCartList() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        Integer inventoryQuantity =  10;
//        for(int i=0;i<4;i++){
//            CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//            cartItemApi.add(cartItemPojo,inventoryQuantity);
//        }
//        List<CartItemPojo> cartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(cartItemPojoList.size(),1);
//
//        for(int i=0;i<4;i++){
//            CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
//            cartItemApi.add(cartItemPojo,inventoryQuantity);
//        }
//        cartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(cartItemPojoList.size(),4);
//    }
//
//    @Test
//    public void testDeleteCartItem() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        for(int i=0;i<4;i++){
//            CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
//            cartItemDao.insert(cartItemPojo);
//        }
//        List<CartItemPojo> cartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(cartItemPojoList.size(),4);
//        cartItemApi.delete(cartItemPojoList.get(0).getCartItemId());
//        List<CartItemPojo> newCartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(newCartItemPojoList.size(),3);
//    }
//
//    @Test
//    public void updateCartItem() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//        cartItemDao.insert(cartItemPojo);
//
//        Double newSellingPrice = 130.62;
//        Integer newQuantity =4;
//        CartItemPojo updatedCartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,newSellingPrice,newQuantity);
//        cartItemApi.update(cartItemPojo, updatedCartItemPojo);
//        CartItemPojo resultCartItemPojo =  cartItemApi.getCheck(cartItemPojo.getCartItemId());
//        assertEquals(resultCartItemPojo.getProductId(),productId);
//        assertEquals(resultCartItemPojo.getCounterId(),counterId);
//        assertEquals(resultCartItemPojo.getProductName(),productName);
//        assertEquals(resultCartItemPojo.getSellingPrice(),newSellingPrice);
//        assertEquals(resultCartItemPojo.getQuantity(),newQuantity);
//    }
//
//    @Test
//    public void testGetCheckCartItem() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//        cartItemDao.insert(cartItemPojo);
//
//        CartItemPojo cartItemPojo1 = cartItemApi.getCheck(cartItemPojo.getCartItemId());
//       assertEquals(cartItemPojo1, cartItemPojo);
//    }
//
//    @Test
//    public void testGetItemsInCurrentCart() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//        cartItemDao.insert(cartItemPojo);
//
//        List<CartItemPojo> cartItemPojoList = cartItemApi.getAll(counterId);
//        assertEquals(cartItemPojoList.size(),1);
//        assertEquals(cartItemPojoList.get(0), cartItemPojo);
//    }
//
//
//    @Test
//    public void testDeleteAllItemsInCurrentCart() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        for(int i=0;i<4;i++){
//            CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
//            cartItemDao.insert(cartItemPojo);
//        }
//        List<CartItemPojo> cartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(cartItemPojoList.size(),4);
//        cartItemApi.deleteAll(cartItemPojoList);
//        List<CartItemPojo> newCartItemPojoList =  cartItemApi.getAllItemsInCart();
//        assertEquals(newCartItemPojoList.size(),0);
//    }
//
//    @Test
//    public void testCartQuantityWithInventoryQuantity() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//        cartItemDao.insert(cartItemPojo);
//
//        Integer inventoryQuantity = 3;
//        CartItemPojo newCartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity+1);
//
//        exceptionRule.expect(ApiException.class);
//        exceptionRule.expectMessage("Item can't be added to cart as it exceeds the inventory. Items already in cart : "+ cartItemPojo.getQuantity() +" Present inventory count :"+inventoryQuantity);
//        cartItemApi.add(newCartItemPojo,inventoryQuantity);
//    }
//
//    @Test
//    public void testProductExistenceInCart() throws ApiException {
//        Integer productId=1;
//        Integer counterId = 1;
//        String productName = "prod1";
//        Double sellingPrice = 120.56;
//        Integer quantity =2;
//        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId,counterId,productName,sellingPrice,quantity);
//        cartItemDao.insert(cartItemPojo);
//
//        exceptionRule.expect(ApiException.class);
//        exceptionRule.expectMessage("No such item exists in cart with given Id");
//        cartItemApi.getCheck(cartItemPojo.getCartItemId()+1);
//    }
//
//}
