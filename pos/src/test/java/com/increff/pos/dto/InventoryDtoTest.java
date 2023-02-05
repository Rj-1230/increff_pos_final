//package com.increff.pos.dto;
//
//import com.increff.pos.api.ApiException;
//import com.increff.pos.config.AbstractUnitTest;
//import com.increff.pos.dao.BrandDao;
//import com.increff.pos.dao.InventoryDao;
//import com.increff.pos.dao.ProductDao;
//import com.increff.pos.model.data.InventoryData;
//import com.increff.pos.model.form.InventoryForm;
//import com.increff.pos.model.form.ProductForm;
//import com.increff.pos.pojo.BrandPojo;
//import com.increff.pos.pojo.InventoryPojo;
//import com.increff.pos.pojo.ProductPojo;
//import com.increff.pos.util.FormUtil;
//import com.increff.pos.util.PojoUtil;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class InventoryDtoTest extends AbstractUnitTest {
//    @Autowired
//    private InventoryDto inventoryDto;
//    @Autowired
//    private InventoryDao inventoryDao;
//    @Autowired
//    private BrandDao brandDao;
//    @Autowired
//    private ProductDao productDao;
//    @Rule
//    public ExpectedException exceptionRule = ExpectedException.none();
//
//    private InventoryForm getDummyInventoryForm(){
//        String barcode = "barcode";
//        Integer quantity = 15;
//        return FormUtil.getInventoryForm(barcode,quantity);
//    }
//
//    private InventoryPojo getDummyInventoryPojo(){
//        Integer productId = 1;
//        String barcode = "barcode";
//        Integer quantity = 10;
//        return PojoUtil.getInventoryPojo(productId,barcode,quantity);
//    }
//
//    @Before
//    public void addBrandAndProduct(){
//        String brand = "test_brand";
//        String category = "test_category";
//        brandDao.insert(PojoUtil.getBrandPojo(brand,category));
//
//        Integer brandId = brandDao.selectAll().get(0).getId();
//        String barcode = "barcode";
//        Double mrp = 110.85;
//        String name = "prod1";
//        productDao.insert(PojoUtil.getProductPojo(brandId,barcode,mrp,name));
//    }
//
//    @Test
//    public void testUpdateInventory() throws ApiException {
//        inventoryDto.updateInventory(getDummyInventoryForm());
//
//        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
//        assertEquals(inventoryPojoList.size(),1);
//        assertEquals(inventoryPojoList.get(0).getBarcode(),"barcode");
//        assertEquals(inventoryPojoList.get(0).getQuantity(),15,0);
//
//    }
////
////    @Test
////    public void testSizeOfProductList() throws ApiException {
////        inventoryDao.insert(getDummyInventoryPojo());
////        List<InventoryData> inventoryDataList = inventoryDto.getAll();
////        assertEquals(inventoryDataList.size(),1);
////    }
////
////    @Test
////    public void testDeleteProduct(){
////        inventoryDao.insert(getDummyInventoryPojo());
////        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
////        assertEquals(inventoryPojoList.size(),1);
////
////        inventoryDto.delete(inventoryPojoList.get(0).getProductId());
////        assertEquals(inventoryDao.selectAll().size(),0);
////    }
////
////    @Test
////    public void testUpdateProduct() throws ApiException {
////        inventoryDao.insert(getDummyInventoryPojo());
////
////        inventoryDto.update(inventoryDao.selectAll().get(0).getProductId(),getDummyProductForm());
////        InventoryPojo updatedInventoryPojo = inventoryDao.selectAll().get(0);
////        assertEquals(updatedInventoryPojo.getName(),"prod1");
////        assertEquals(updatedInventoryPojo.getMrp(),120.56,0.00);
////    }
////
////
////    @Test
////    public void testGetProduct() throws ApiException {
////        inventoryDao.insert(getDummyInventoryPojo());
////        Integer productId = inventoryDao.selectAll().get(0).getProductId();
////        InventoryData inventoryData = inventoryDto.get(productId);
////        assertEquals(inventoryData.getBarcode(),"barcode");
////        assertEquals(inventoryData.getBrand(),"test_brand");
////        assertEquals(inventoryData.getCategory(),"test_category");
////        assertEquals(inventoryData.getName(),"prod2");
////        assertEquals(inventoryData.getMrp(),110.85,0.00);
////    }
//
//}
//
