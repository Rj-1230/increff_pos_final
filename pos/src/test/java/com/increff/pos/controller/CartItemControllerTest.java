package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartItemDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.form.CartItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FormUtil;
import com.increff.pos.util.PojoUtil;
import com.increff.pos.util.UserPrincipal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import static com.increff.pos.util.DummyForm.*;

import static org.junit.Assert.assertEquals;

public class CartItemControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private CartItemController cartItemController;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private CartItemDao cartItemDao;

    @Before
    public void setup() {
        //Authentication mocking
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);

        //Adding brand
        String brand = "brand";
        String category = "category";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand, category);
        brandDao.insert(brandPojo);

        //Adding a product
        Integer brandId = brandDao.selectAll().get(0).getBrandId();
        String barcode = "barcode";
        String productName = "prod1";
        Double mrp = 124.54;
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId, barcode, mrp, productName);
        productDao.insert(productPojo);

        //Adding inventory corresponding to above product since it was added in flow layer
        Integer productId = productDao.selectAll().get(0).getProductId();
        Integer quantity = 80;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId, quantity);
        inventoryDao.insert(inventoryPojo);
    }


    @Test
    public void testAddCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        List<CartItemData> cartItemDataList = cartItemController.getAll();

        assertEquals(1, cartItemDataList.size());
        CartItemData cartItemData = cartItemDataList.get(0);
        assertEquals(productDao.selectAll().get(0).getProductId(), cartItemData.getProductId());
        assertEquals("prod1", cartItemData.getProductName());
        assertEquals(new Double(120.66), cartItemData.getSellingPrice());
        assertEquals(new Integer(10), cartItemData.getQuantity());
    }

    @Test
    public void testDeleteCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        List<CartItemData> cartItemDataList = cartItemController.getAll();
        assertEquals(1, cartItemDataList.size());
        cartItemController.delete(cartItemDataList.get(0).getCartItemId());
        assertEquals(0, cartItemController.getAll().size());
    }

    @Test
    public void testUpdateCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemForm updatedCartItemForm = FormUtil.getCartItemForm("barcode", 8, 121.44);
        cartItemController.update(cartItemController.getAll().get(0).getCartItemId(), updatedCartItemForm);
        List<CartItemData> cartItemDataList = cartItemController.getAll();

        CartItemData cartItemData = cartItemDataList.get(0);
        assertEquals(productDao.selectAll().get(0).getProductId(), cartItemData.getProductId());
        assertEquals("prod1", cartItemData.getProductName());
        assertEquals(new Double(121.44), cartItemData.getSellingPrice());
        assertEquals(new Integer(8), cartItemData.getQuantity());
    }

    @Test
    public void testGetCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemData cartItemData = cartItemController.getAll().get(0);
        CartItemData cartItemData1 = cartItemController.get(cartItemDao.selectAll().get(0).getCartItemId());
        assertEquals(cartItemData1.getCartItemId(), cartItemData.getCartItemId());
        assertEquals(cartItemData1.getProductName(), cartItemData.getProductName());
        assertEquals(cartItemData1.getQuantity(), cartItemData.getQuantity());
        assertEquals(cartItemData1.getSellingPrice(), cartItemData.getSellingPrice());
        assertEquals(cartItemData1.getProductId(), cartItemData.getProductId());
        assertEquals(cartItemData1.getCounterId(), cartItemData.getCounterId());

    }

    @Test
    public void testDeleteAll() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        cartItemController.add(FormUtil.getCartItemForm("barcode", 1, 110.66));
        assertEquals(1, cartItemController.getAll().size());
        cartItemController.deleteAllCartItems();
        assertEquals(0, cartItemController.getAll().size());

    }

    @Test
    public void checkMrpWhileAddCartItem() throws ApiException {
        CartItemForm cartItemForm = getDummyCartItemForm();
        cartItemForm.setSellingPrice(150.66);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling price must be less than MRP. Product's MRP :" + new Double(124.54));
        cartItemController.add(cartItemForm);
    }

    @Test
    public void checkInventoryWhileAddCartItem() throws ApiException {
        CartItemForm cartItemForm = getDummyCartItemForm();
        cartItemForm.setQuantity(99);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Not sufficient inventory. Present inventory count : " + new Integer(80));
        cartItemController.add(cartItemForm);
    }

    @Test
    public void checkMrpWhileUpdateCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemForm updatedCartItemForm = FormUtil.getCartItemForm("barcode", 10, 1241.44);


        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling price must be less than MRP. Product's MRP :" + new Double(124.54));
        cartItemController.update(cartItemController.getAll().get(0).getCartItemId(), updatedCartItemForm);
    }


    @Test
    public void checkInventoryWhileUpdateCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemForm updatedCartItemForm = FormUtil.getCartItemForm("barcode", 99, 121.44);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Not sufficient inventory. Present inventory count : " + new Integer(80));
        cartItemController.update(cartItemController.getAll().get(0).getCartItemId(), updatedCartItemForm);
    }

    @Test
    public void testBarcodeNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode field can't be null");
        cartItemController.add(FormUtil.getCartItemForm(null,2,120.55));
    }
    @Test
    public void testQuantityNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The quantity field can't be null");
        cartItemController.add(FormUtil.getCartItemForm("barcode",null,120.55));
    }
    @Test
    public void testSellingPriceNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The selling price field can't be null");
        cartItemController.add(FormUtil.getCartItemForm("barcode",2,null));
    }
    @Test
    public void testBarcodeNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode can't be empty");
        cartItemController.add(FormUtil.getCartItemForm("        ",2,120.77));
    }
    @Test
    public void testQuantityPositive() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The item quantity to be added in the cart must be positive");
        cartItemController.add(FormUtil.getCartItemForm("barcode",-9,120.77));
    }
    @Test
    public void testQuantityRange() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Maximum 99 items can be added to cart at once");
        cartItemController.add(FormUtil.getCartItemForm("barcode",120,120.77));
    }
    @Test
    public void testSellingPricePositive() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The selling price of item must be positive");
        cartItemController.add(FormUtil.getCartItemForm("barcode",9,-2.45));
    }
    @Test
    public void testSellingPriceRange() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The selling price of item must be less than 999999");
        cartItemController.add(FormUtil.getCartItemForm("barcode",12,new Double(10000000)));
    }

    @Test
    public void testCartQuantityWithInventoryQuantity() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemForm cartItemForm = FormUtil.getCartItemForm("barcode",75,120.44);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Item can't be added to cart as it exceeds the inventory. Items already in cart : " + cartItemController.getAll().get(0).getQuantity() + " Present inventory count :" + inventoryDao.selectAll().get(0).getQuantity());
        cartItemController.add(cartItemForm);
    }

    @Test
    public void testProductExistenceInCart() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such item exists in cart with given Id");
        cartItemController.get(cartItemController.getAll().get(0).getCartItemId() + 3);
    }
}
