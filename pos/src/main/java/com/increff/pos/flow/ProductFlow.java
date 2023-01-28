package com.increff.pos.flow;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.ProductDtoHelper.convert;

@Service
public class ProductFlow {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo,String brand,String category) throws ApiException {
//        Putting brandId inside productPojo
        int brandId = brandService.getBrandIdFromName(brand,category);
        productPojo.setBrandId(brandId);

        int productId = productService.add(productPojo);

//        Creating an inventory corresponding to the product added with quantity 0
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(productPojo.getBarcode());
        inventoryService.addNewItemToInventory(inventoryPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.getCheck(id);
        BrandPojo brandPojo = brandService.getCheckBrand(productPojo.getBrandId());
        ProductData productData = convert(productPojo);
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        return productData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo productPojo: productPojoList){
            BrandPojo brandPojo = brandService.getCheckBrand(productPojo.getBrandId());
            ProductData productData = convert(productPojo);
            productData.setBrand(brandPojo.getBrand());
            productData.setCategory(brandPojo.getCategory());
            list2.add(productData);
        }
        return list2;
    }

}


