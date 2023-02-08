package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import static com.increff.pos.util.DummyForm.*;
import com.increff.pos.pojo.BrandPojo;
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

public class ProductControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private ProductController productController;
    @Autowired
    private BrandDao brandDao;

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
    }


    @Test
    public void testAddProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        List<ProductData> productDataList = productController.getAll();
        assertEquals(1, productDataList.size());
        assertEquals("brand", productDataList.get(0).getBrand());
        assertEquals("category", productDataList.get(0).getCategory());
        assertEquals("prod1", productDataList.get(0).getName());
        assertEquals(new Double(124.54), productDataList.get(0).getMrp());

    }

    @Test
    public void testGetProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductData productData = productController.getAll().get(0);
        ProductData productData1 = productController.get(productData.getProductId());
        assertEquals(productData1.getBrand(), productData.getBrand());
        assertEquals(productData1.getCategory(), productData.getCategory());
        assertEquals(productData1.getBarcode(), productData.getBarcode());
        assertEquals(productData1.getMrp(), productData.getMrp());
        assertEquals(productData1.getMrp(), productData.getMrp());
    }

    @Test
    public void testUpdateProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductForm updatedProductForm = FormUtil.getProductForm("barcode", "brand", "category", "prod2", 140.88);
        ProductData productData = productController.getAll().get(0);
        productController.update(productData.getProductId(), updatedProductForm);
        ProductData productData1 = productController.get(productData.getProductId());
        assertEquals("prod2", productData1.getName());
        assertEquals(new Double(140.88), productData1.getMrp());
        assertEquals(productData.getProductId(), productData1.getProductId());
    }

    @Test
    public void testSizeOfProductList() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductForm newProductForm = FormUtil.getProductForm("barcode1", "brand", "category", "new_prod1", 130.88);
        productController.add(newProductForm);
        assertEquals(2, productController.getAll().size());
    }

    @Test
    public void testBrandCategoryExistenceFromBrandCategoryName() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("There is no entry with brand newbrand and category newcategory");
        productController.add(FormUtil.getProductForm("barcode1","newbrand","newcategory","testproduct",120.86));
    }

    @Test
    public void testBarcodeNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode field can't be null");
        productController.add(FormUtil.getProductForm(null,"brand","category","prod",100.2));
    }
    @Test
    public void testBrandCategoryNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The brand name or category name field can't be null");
        productController.add(FormUtil.getProductForm("barcode",null,"category","prod",100.2));
    }    @Test
    public void testProductNameNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The product name field can't be null");
        productController.add(FormUtil.getProductForm("barcode","brand","category",null,100.2));
    }
    @Test
    public void testMRPNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The MRP of product can't be null");
        productController.add(FormUtil.getProductForm("barcode","brand","category","prod",null));
    }

    @Test
    public void testBarcodeNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The barcode can't be empty");
        productController.add(FormUtil.getProductForm("     ","brand","category","prod",100.54));
    }

    @Test
    public void testProductNameNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The product name field can't be empty");
        productController.add(FormUtil.getProductForm("barcode","brand","category","   ",100.66));
    }

    @Test
    public void testMrpPositive() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The MRP of product can't be 0 or negative");
        productController.add(FormUtil.getProductForm("barcode","brand","category","prod",-2.66));
    }

    @Test
    public void testMrpRange() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The MRP of product must be less than 999999");
        productController.add(FormUtil.getProductForm("barcode","brand","category","prod",new Double(1000000.66)));
    }

}
