package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
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

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private BrandDao brandDao;
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

    private BrandPojo getDummyBrandPojo(){
        String brand = "test_brand";
        String category = "test_category";
        return PojoUtil.getBrandPojo(brand,category);
    }
    @Test
    public void testAddBrand() throws ApiException {
        BrandForm brandForm = FormUtil.getBrandForm("brand","category");
        brandDto.addBrand(brandForm);
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        assertEquals(brandPojoList.size(),1);
        assertEquals(brandPojoList.get(0).getBrand(),"brand");
        assertEquals(brandPojoList.get(0).getCategory(),"category");
    }

    @Test
    public void testSizeOfBrandList() throws ApiException {
        String brand= "test_brand";
        String category ="test_category";
        for(int i=0;i<4;i++){
            BrandPojo brandPojo = PojoUtil.getBrandPojo(brand+i,category+i);
            brandDao.insert(brandPojo);
        }
        List<BrandData> brandDataList = brandDto.getAll();
        assertEquals(brandDataList.size(),4);
    }

    @Test
    public void testUpdateBrand() throws ApiException {
        BrandPojo brandPojo = getDummyBrandPojo();
        brandDao.insert(brandPojo);

        Integer id = brandDao.selectAll().get(0).getBrandId();
        brandDto.updateBrand(id,getDummyBrandForm());
        List<BrandPojo> brandPojoList = brandDao.selectAll();
        assertEquals(brandPojoList.get(0).getBrand(),"brand");
        assertEquals(brandPojoList.get(0).getCategory(),"category");
    }


    @Test
    public void testGetBrand() throws ApiException {
        BrandPojo brandPojo = getDummyBrandPojo();
        brandDao.insert(brandPojo);

        BrandData brandData= brandDto.getBrand(brandPojo.getBrandId());
        assertEquals(brandData.getBrand(),"test_brand");
        assertEquals(brandData.getCategory(),"test_category");
    }
}
