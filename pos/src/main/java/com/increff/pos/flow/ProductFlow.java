package com.increff.pos.flow;

import com.increff.pos.model.data.ProductData;
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

import static com.increff.pos.helper.flowHelper.ProductFlowHelper.convertProductPojoToProductData;

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
        Integer brandId = brandApi.getCheckBrand(brand,category);
        productPojo.setBrandId(brandId);
        Integer productId = productApi.add(productPojo);
        initializeInventoryForProduct(productId);
    }


    @Transactional(rollbackOn = ApiException.class)
    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productApi.getCheckProduct(id);
        BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
        return convertProductPojoToProductData(productPojo,brandPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productApi.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for(ProductPojo productPojo: productPojoList){
            BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
            list2.add(convertProductPojoToProductData(productPojo,brandPojo));
        }
        return list2;
    }

    private void initializeInventoryForProduct(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(0);
        inventoryApi.addNewItemToInventory(inventoryPojo);
    }

}


