package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FormUtil;
import com.increff.pos.util.PojoUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BrandDao brandDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private ProductForm getDummyProductForm(){
        String barcode = "barcode";
        String brandName = "test_brand";
        String categoryName ="test_category";
        Double mrp = 120.56;
        String name = "prod1";
        return FormUtil.getProductForm(barcode,brandName,categoryName,name,mrp);
    }

    private ProductPojo getDummyProductPojo(){
        Integer brandId = 1;
        String barcode = "barcode";
        Double mrp = 110.85;
        String name = "prod2";
        return PojoUtil.getProductPojo(brandId,barcode,mrp,name);
    }

    @Before
    public void addBrand(){
        String brand = "test_brand";
        String category = "test_category";
        brandDao.insert(PojoUtil.getBrandPojo(brand,category));
    }

    @Test
    public void testAddProduct() throws ApiException {
        productDto.addProduct(getDummyProductForm());
        List<ProductData> productDataList = productDto.getAllProducts();
        assertEquals(productDataList.size(),1);
        assertEquals("barcode",productDataList.get(0).getBarcode());
        assertEquals(new Double(120.56),productDataList.get(0).getMrp());
        assertEquals("prod1",productDataList.get(0).getName());

    }

//    @Test
//    public void testSizeOfProductList() throws ApiException {
//        productDto.addProduct(getDummyProductForm());
//        List<ProductData> productDataList = productDto.getAll();
//        assertEquals(productDataList.size(),1);
//    }
//
//    @Test
//    public void testDeleteProduct(){
//        productDao.insert(getDummyProductPojo());
//        List<ProductPojo> productDataList = productDao.selectAll();
//        assertEquals(productDataList.size(),1);
//
//        productDto.delete(productDataList.get(0).getProductId());
//        assertEquals(productDao.selectAll().size(),0);
//    }
//
//    @Test
//    public void testUpdateProduct() throws ApiException {
//        productDao.insert(getDummyProductPojo());
//
//        productDto.update(productDao.selectAll().get(0).getProductId(),getDummyProductForm());
//        ProductPojo updatedProductPojo = productDao.selectAll().get(0);
//        assertEquals(updatedProductPojo.getName(),"prod1");
//        assertEquals(updatedProductPojo.getMrp(),120.56,0.00);
//    }
//
//
//    @Test
//    public void testGetProduct() throws ApiException {
//       productDao.insert(getDummyProductPojo());
//       Integer productId = productDao.selectAll().get(0).getProductId();
//       BrandPojo brandPojo = brandDao.selectAll().get(0);
//       ProductData productData = productDto.get(productId);
//       assertEquals(productData.getBarcode(),"barcode");
//       assertEquals(productData.getBrand(),"test_brand");
//       assertEquals(productData.getCategory(),"test_category");
//       assertEquals(productData.getName(),"prod2");
//       assertEquals(productData.getMrp(),110.85,0.00);
//    }

}
