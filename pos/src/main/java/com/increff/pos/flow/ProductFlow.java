package com.increff.pos.flow;

import com.increff.pos.model.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.ProductDtoHelper.convert;

@Service
public class ProductFlow {
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private BrandApi brandApi;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo,String brand,String category) throws ApiException {
//        Putting brandId inside productPojo
        Integer brandId = brandApi.getBrandIdFromName(brand,category);
        productPojo.setBrandId(brandId);

        Integer productId = productApi.add(productPojo);

//        Creating an inventory corresponding to the product added with quantity 0
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(productPojo.getBarcode());
        inventoryApi.addNewItemToInventory(inventoryPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productApi.getCheck(id);
        BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
        ProductData productData = convert(productPojo);
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        return productData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productApi.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo productPojo: productPojoList){
            BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
            ProductData productData = convert(productPojo);
            productData.setBrand(brandPojo.getBrand());
            productData.setCategory(brandPojo.getCategory());
            list2.add(productData);
        }
        return list2;
    }

}


