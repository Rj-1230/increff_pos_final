package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
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
    public void add(@RequestBody ProductForm f)throws ApiException{
            productDto.add(f);
    }

    @ApiOperation(value="Deleting a product")
    @RequestMapping(path="/api/supervisor/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id){
        productDto.delete(id);
    }

    @ApiOperation(value="Getting details of a product from id")
    @RequestMapping(path="/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        return productDto.get(id);
    }

    @ApiOperation(value="Updating details of a particular Product")
    @RequestMapping(path="/api/supervisor/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm f) throws ApiException {
        productDto.update(id,f);
    }

    @ApiOperation(value="Getting details of all the products")
    @RequestMapping(path="/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll()throws ApiException{
        return productDto.getAll();
    }

}
