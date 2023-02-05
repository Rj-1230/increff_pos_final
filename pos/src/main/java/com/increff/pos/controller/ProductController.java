package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api

public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value="Adding a product")
    @RequestMapping(path="/api/supervisor/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm)throws ApiException{
            productDto.addProduct(productForm);
    }

    @ApiOperation(value="Deleting a product")
    @RequestMapping(path="/api/supervisor/product/{productId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer productId){
        productDto.deleteProduct(productId);
    }

    @ApiOperation(value="Getting details of a product from productId")
    @RequestMapping(path="/api/product/{productId}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer productId) throws ApiException {
        return productDto.getProduct(productId);
    }

    @ApiOperation(value="Updating details of a particular Product")
    @RequestMapping(path="/api/supervisor/product/{productId}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer productId, @RequestBody ProductForm productForm) throws ApiException {
        productDto.updateProduct(productId,productForm);
    }

    @ApiOperation(value="Getting details of all the products")
    @RequestMapping(path="/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll()throws ApiException{
        return productDto.getAllProducts();
    }

}
