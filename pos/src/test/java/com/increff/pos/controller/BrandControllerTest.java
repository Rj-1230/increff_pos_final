package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.FormUtil;
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
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class BrandControllerTest extends AbstractUnitTest {

    @Autowired
    private BrandController brandController;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void getAuthenticationDetails(){
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication =  Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }
    private BrandForm getDummyBrandForm(){
        String brand = "brand";
        String category = "category";
        return FormUtil.getBrandForm(brand,category);
    }

    @Test
    public void testAddBrand() throws ApiException {
        BrandForm brandForm = getDummyBrandForm();
        brandController.add(brandForm);

        List<BrandData> brandDataList = brandController.getAll();
        assertEquals(1, brandDataList.size());
        assertEquals("brand",brandDataList.get(0).getBrand());
        assertEquals("category",brandDataList.get(0).getCategory());
    }

    @Test
    public void testGetBrand() throws ApiException {
        BrandForm brandForm = getDummyBrandForm();
        brandController.add(brandForm);

        BrandData brandData = brandController.getAll().get(0);
        BrandData brandData1 = brandController.get(brandData.getId());
        assertEquals(brandData1.getBrand(),brandData.getBrand());
        assertEquals(brandData1.getCategory(),brandData.getCategory());
        assertEquals(brandData1.getId(),brandData.getId());
    }

    @Test
    public void testUpdateBrand() throws ApiException {
        BrandForm brandForm = getDummyBrandForm();
        brandController.add(brandForm);

        BrandForm updatedBrandForm = FormUtil.getBrandForm("test_brand","test_category");
        BrandData brandData = brandController.getAll().get(0);
        brandController.update(brandData.getId(),updatedBrandForm);
        BrandData brandData1 = brandController.get(brandData.getId());
        assertEquals("test_brand",brandData1.getBrand());
        assertEquals("test_category",brandData1.getCategory());
    }

    @Test
    public void testSizeOfBrandList() throws ApiException {
        BrandForm brandForm = getDummyBrandForm();
        brandController.add(brandForm);

        BrandForm newBrandForm = FormUtil.getBrandForm("test_brand","test_category");
        brandController.add(newBrandForm);
        assertEquals(2,brandController.getAll().size());

    }
}
