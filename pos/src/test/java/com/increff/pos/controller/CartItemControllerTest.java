package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.CartItemDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.CartItemForm;
import com.increff.pos.model.form.InventoryForm;
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

import static org.junit.Assert.assertEquals;

public class CartItemControllerTest extends AbstractUnitTest {

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
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void getAuthenticationDetails(){
        //Authentication mocking
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication =  Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);

        //Adding brand
        String brand = "brand";
        String category = "category";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

        //Adding a product
        Integer brandId = brandDao.selectAll().get(0).getBrandId();
        String barcode = "barcode";
        String productName = "prod1";
        Double mrp = 124.54;
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        //Adding inventory corresponding to above product since it was added in flow layer
        Integer productId = productDao.selectAll().get(0).getProductId();
        Integer quantity = 100;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);
    }
    private CartItemForm getDummyCartItemForm(){
        String barcode = "barcode";
        Integer quantity =  10;
        Double sellingPrice = 120.66;
        return FormUtil.getCartItemForm(barcode,quantity,sellingPrice);
    }


    @Test
    public void testAddCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        List<CartItemData> cartItemDataList = cartItemController.getAll();

        assertEquals(1,cartItemDataList.size());
        CartItemData cartItemData = cartItemDataList.get(0);
        assertEquals(productDao.selectAll().get(0).getProductId(),cartItemData.getProductId());
        assertEquals("prod1",cartItemData.getProductName());
        assertEquals(new Double(120.66),cartItemData.getSellingPrice());
        assertEquals(new Integer(10),cartItemData.getQuantity());
    }

    @Test
    public void testDeleteCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        List<CartItemData> cartItemDataList = cartItemController.getAll();
        assertEquals(1,cartItemDataList.size());
        cartItemController.delete(cartItemDataList.get(0).getCartItemId());
        assertEquals(0,cartItemController.getAll().size());
    }

    @Test
    public void testUpdateCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemForm updatedCartItemForm = FormUtil.getCartItemForm("barcode",8,121.44);
        cartItemController.update(cartItemController.getAll().get(0).getCartItemId(),updatedCartItemForm);
        List<CartItemData> cartItemDataList = cartItemController.getAll();

        CartItemData cartItemData = cartItemDataList.get(0);
        assertEquals(productDao.selectAll().get(0).getProductId(),cartItemData.getProductId());
        assertEquals("prod1",cartItemData.getProductName());
        assertEquals(new Double(121.44),cartItemData.getSellingPrice());
        assertEquals(new Integer(8),cartItemData.getQuantity());
    }

    @Test
    public void testGetCartItem() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        CartItemData cartItemData = cartItemController.getAll().get(0);
        CartItemData cartItemData1 = cartItemController.get(cartItemDao.selectAll().get(0).getCartItemId());
        System.out.println(cartItemData1.getProductName());
        assertEquals(cartItemData1.getCartItemId(),cartItemData.getCartItemId());
        assertEquals(cartItemData1.getProductName(),cartItemData.getProductName());
        assertEquals(cartItemData1.getQuantity(),cartItemData.getQuantity());
        assertEquals(cartItemData1.getSellingPrice(),cartItemData.getSellingPrice());
        assertEquals(cartItemData1.getProductId(),cartItemData.getProductId());
        assertEquals(cartItemData1.getCounterId(),cartItemData.getCounterId());

    }

    @Test
    public void testDeleteAll() throws ApiException {
        cartItemController.add(getDummyCartItemForm());
        cartItemController.add(FormUtil.getCartItemForm("barcode",1,110.66));
        assertEquals(1,cartItemController.getAll().size());
        cartItemController.deleteAllCartItems();
        assertEquals(0,cartItemController.getAll().size());

    }
}
