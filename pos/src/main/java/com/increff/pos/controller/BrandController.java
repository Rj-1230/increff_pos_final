package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api

public class BrandController {
    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value="Adding a brand")
    @RequestMapping(path="/api/supervisor/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm f) throws ApiException {
            brandDto.addBrand(f);
    }

    //    Although delete is disabled from UI, but method made for future use
    @ApiOperation(value="Deleting a brand")
    @RequestMapping(path="/api/supervisor/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        brandDto.deleteBrand(id);
    }

    @ApiOperation(value="Getting details of a brand from brandId")
    @RequestMapping(path="/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return brandDto.getBrand(id);
    }

    @ApiOperation(value="Updating details of a particular brand-category combo")
    @RequestMapping(path="/api/supervisor/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        brandDto.updateBrand(id,f);
    }

    @ApiOperation(value="Getting details of all the brand-category")
    @RequestMapping(path="/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

}
