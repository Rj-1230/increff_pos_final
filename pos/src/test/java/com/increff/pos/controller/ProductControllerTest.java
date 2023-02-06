package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
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

    @Autowired
    private ProductController productController;
    @Autowired
    private BrandDao brandDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void getAuthenticationDetails(){
        //Authentication mocking
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
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
    }
    private ProductForm getDummyProductForm(){
        String barcode = "barcode";
        String brand = "brand";
        String category = "category";
        String productName = "prod1";
        Double mrp = 124.54;
        return FormUtil.getProductForm(barcode,brand,category,productName,mrp);
    }

    @Test
    public void testAddProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        List<ProductData> productDataList = productController.getAll();
        assertEquals(1,productDataList.size());
        assertEquals("brand",productDataList.get(0).getBrand());
        assertEquals("category",productDataList.get(0).getCategory());
        assertEquals("prod1",productDataList.get(0).getName());
        assertEquals(new Double(124.54),productDataList.get(0).getMrp());

    }

    @Test
    public void testGetProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductData productData = productController.getAll().get(0);
        ProductData productData1 = productController.get(productData.getProductId());
        assertEquals(productData1.getBrand(),productData.getBrand());
        assertEquals(productData1.getCategory(),productData.getCategory());
        assertEquals(productData1.getBarcode(),productData.getBarcode());
        assertEquals(productData1.getMrp(),productData.getMrp());
        assertEquals(productData1.getMrp(),productData.getMrp());
    }

    @Test
    public void testUpdateProduct() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductForm updatedProductForm = FormUtil.getProductForm("barcode","brand","category","prod2",140.88);
        ProductData productData = productController.getAll().get(0);
        productController.update(productData.getProductId(),updatedProductForm);
        ProductData productData1 = productController.get(productData.getProductId());
        assertEquals("prod2",productData1.getName());
        assertEquals(new Double(140.88),productData1.getMrp());
        assertEquals(productData.getProductId(),productData1.getProductId());
    }

    @Test
    public void testSizeOfProductList() throws ApiException {
        ProductForm productForm = getDummyProductForm();
        productController.add(productForm);

        ProductForm newProductForm = FormUtil.getProductForm("barcode1","brand","category","new_prod1",130.88);
        productController.add(newProductForm);
        assertEquals(2,productController.getAll().size());

    }
}
