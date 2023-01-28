package com.increff.pos.dto;

import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.helper.ProductDtoHelper.*;

import java.util.List;

@Service

public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductFlow productFlow;


    public void add(ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productFlow.add(convert(f),f.getBrandName(),f.getCategoryName());
    }

    public void delete(@PathVariable int id){
        productService.delete(id);
    }

    public ProductData get(int id) throws ApiException {
        return productFlow.get(id);
    }

    public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productService.update(id,convert(f));
    }

    public List<ProductData> getAll()throws ApiException{
        return productFlow.getAll();
    }
}