package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.InventoryData;
import static com.increff.pos.util.DummyForm.*;
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

public class InventoryControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private InventoryController inventoryController;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    @Before
    public void setup() {
        //Authentication mocking
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
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
        Integer quantity = 0;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId, quantity);
        inventoryDao.insert(inventoryPojo);
    }



    @Test
    public void testUpdateInventory() throws ApiException {
        inventoryController.update(getDummyInventoryForm());
        List<InventoryData> inventoryDataList = inventoryController.getAll();

        assertEquals(1, inventoryDataList.size());
        InventoryData inventoryData = inventoryDataList.get(0);
        assertEquals("prod1", inventoryData.getProductName());
        assertEquals("barcode", inventoryData.getBarcode());
        assertEquals(new Integer(10), inventoryData.getQuantity());
        assertEquals(productDao.selectAll().get(0).getProductId(), inventoryData.getProductId());
    }

    @Test
    public void testGetInventory() throws ApiException {
        InventoryData inventoryData = inventoryController.getAll().get(0);
        InventoryData inventoryData1 = inventoryController.get(inventoryDao.selectAll().get(0).getInventoryId());
        assertEquals(inventoryData1.getProductName(), inventoryData.getProductName());
        assertEquals(inventoryData1.getBarcode(), inventoryData.getBarcode());
        assertEquals(inventoryData1.getQuantity(), inventoryData.getQuantity());
        assertEquals(inventoryData1.getProductId(), inventoryData.getProductId());
    }

    @Test
    public void testBarcodeNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode field can't be null");
        inventoryController.update(FormUtil.getInventoryForm(null,2));
    }
    @Test
    public void testQuantityNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The quantity field can't be null");
        inventoryController.update(FormUtil.getInventoryForm("barcode",null));
    }

    @Test
    public void testBarcodeNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode can't be empty");
        inventoryController.update(FormUtil.getInventoryForm("     ",10));
    }

    @Test
    public void testQuantityPositive() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The item quantity in the inventory must be positive");
        inventoryController.update(FormUtil.getInventoryForm("barcode",-2));
    }

    @Test
    public void testQuantityRange() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Maximum 999 items can be added to inventory at once");
        inventoryController.update(FormUtil.getInventoryForm("barcode",2000));
    }

}
