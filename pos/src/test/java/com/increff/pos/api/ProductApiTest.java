package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductApiTest extends AbstractUnitTest{
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductDao productDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddProduct() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productApi.add(productPojo);

        List<ProductPojo> productPojoList =  productApi.getAll();
        assertEquals(productPojoList.size(),1);
        assertEquals(productPojoList.get(0).getBrandId(),brandId);
        assertEquals(productPojoList.get(0).getBarcode(),barcode);
        assertEquals(productPojoList.get(0).getMrp(),mrp);
        assertEquals(productPojoList.get(0).getName(),productName);
    }
    @Test
    public void testSizeOfProductList() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        for(int i=0;i<4;i++){
            ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode+i,mrp,productName);
            productDao.insert(productPojo);
        }
        List<ProductPojo> productPojoList =  productApi.getAll();
        assertEquals(productPojoList.size(),4);
    }


    @Test
    public void testDeleteProduct() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        for(int i=0;i<4;i++){
            ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode+i,mrp,productName);
            productDao.insert(productPojo);
        }
        List<ProductPojo> productPojoList =  productApi.getAll();
        assertEquals(productPojoList.size(),4);
        productApi.delete(productPojoList.get(0).getProductId());
        List<ProductPojo> newProductPojoList =  productApi.getAll();
        assertEquals(newProductPojoList.size(),3);
    }

    @Test
    public void updateProduct() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        Double newMrp= 10.25;
        String newProductName= "prod2";
        ProductPojo updatedProductPojo = PojoUtil.getProductPojo(brandId,barcode,newMrp,newProductName);
        productApi.update(productPojo.getProductId(), updatedProductPojo);
        List<ProductPojo> productPojoList =  productApi.getAll();
        assertEquals(productPojoList.get(0).getBrandId(),brandId);
        assertEquals(productPojoList.get(0).getBarcode(),barcode);
        assertEquals(productPojoList.get(0).getMrp(),newMrp);
        assertEquals(productPojoList.get(0).getName(),newProductName);
    }

    @Test
    public void testGetCheckProduct() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = productApi.getCheckProduct(productPojo.getProductId());
        assertEquals(productPojo1.getBrandId(),brandId);
        assertEquals(productPojo1.getBarcode(),barcode);
        assertEquals(productPojo1.getMrp(),mrp);
        assertEquals(productPojo1.getName(),productName);
    }

    @Test
    public void testGetProductPojoFromBarcode() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = productApi.getCheckProductPojoFromBarcode(barcode);
        assertEquals(productPojo1,productPojo);
    }

    @Test
    public void testBarcodeUniqueness() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        Integer newBrandId = 2;
        String newBarcode = "abc";
        Double newMrp= 10.25;
        String newProductName= "prod2";
        ProductPojo newProductPojo = PojoUtil.getProductPojo(newBrandId,newBarcode,newMrp,newProductName);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The product with given barcode already exists");
        productApi.add(newProductPojo);
    }

    @Test
    public void testProductExistence() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such product with given id exists !");
        productApi.getCheckProduct(productPojo.getProductId()+1);
    }

    @Test
    public void testProductWithWrongBarcode() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The product with given barcode doesn't exists");
        productApi.getCheckProductPojoFromBarcode("barcode");
    }


    @Test
    public void testGetProduct() throws ApiException {
        Integer brandId = 1;
        String barcode = "abc";
        Double mrp= 12.35;
        String productName= "prod1";
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId,barcode,mrp,productName);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = productApi.get(productPojo.getProductId());
        assertEquals(productPojo1,productPojo);
    }

}
