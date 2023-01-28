package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest{
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandDao brandDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddBrand() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand, category);
        brandService.addBrand(brandPojo);

        List<BrandPojo> brandPojoList =  brandService.getAll();
        assertEquals(brandPojoList.size(),1);
        assertEquals(brandPojoList.get(0).getBrand(),brand);
        assertEquals(brandPojoList.get(0).getCategory(),category);
    }

    @Test
    public void testSizeOfBrandList() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        for(int i=0;i<4;i++){
            BrandPojo brandPojo = PojoUtil.getBrandPojo(brand+i,category+i);
            brandDao.insert(brandPojo);
        }
        List<BrandPojo> brandPojoList =  brandService.getAll();
        assertEquals(brandPojoList.size(),4);
    }

    @Test
    public void testDeleteBrandCategory() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        for(int i=0;i<4;i++){
            BrandPojo brandPojo = PojoUtil.getBrandPojo(brand+i,category+i);
            brandDao.insert(brandPojo);
        }
        List<BrandPojo> brandPojoList =  brandService.getAll();
        assertEquals(brandPojoList.size(),4);
         brandService.deleteBrand(brandPojoList.get(0).getId());
        List<BrandPojo> newBrandPojoList =  brandService.getAll();
        assertEquals(newBrandPojoList.size(),3);
    }

    @Test
    public void updateBrandCategory() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

        String updatedBrand = "brand";
        String updatedCategory = "category";
        BrandPojo updatedBrandPojo = PojoUtil.getBrandPojo(updatedBrand,updatedCategory);
        brandService.updateBrand(brandPojo.getId(), updatedBrandPojo);
        List<BrandPojo> brandPojoList =  brandService.getAll();
        assertEquals(brandPojoList.get(0).getBrand(),updatedBrand);
        assertEquals(brandPojoList.get(0).getCategory(),updatedCategory);
    }

    @Test
    public void testGetCheckBrandCategory() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

       BrandPojo brandPojo1 = brandService.getCheckBrand(brandPojo.getId());
        assertEquals(brandPojo1.getBrand(),brand);
        assertEquals(brandPojo1.getCategory(),category);
    }

    @Test
    public void testGetBrandIdFromName() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

        Integer brandId = brandService.getBrandIdFromName(brand,category);
        assertEquals(brandPojo.getId(),brandId);
    }
@Test
    public void testBrandCategoryUniqueness() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The given brand-category already exists");
        brandService.addBrand(brandPojo);


        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The given brand-category already exists");
        brandService.updateBrand(brandPojo.getId(),brandPojo);
    }


    @Test
    public void testBrandCategoryExistence() throws ApiException {
        String brand= "testBrand";
        String category ="testCategory";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand,category);
        brandDao.insert(brandPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such brand-category with given id exists !");
        brandService.getCheckBrand(brandPojo.getId()+1);


        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The given brand-category does not exist");
        brandService.getBrandIdFromName("brand","category");
    }
}
