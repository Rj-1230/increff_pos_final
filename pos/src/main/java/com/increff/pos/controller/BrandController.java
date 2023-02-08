package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
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

    @ApiOperation(value = "Adding a brand")
    @RequestMapping(path = "/api/supervisor/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        brandDto.addBrand(brandForm);
    }

    @ApiOperation(value = "Getting details of a brand from brandId")
    @RequestMapping(path = "/api/brand/{brandId}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer brandId) throws ApiException {
        return brandDto.getBrand(brandId);
    }

    @ApiOperation(value = "Updating details of a particular brand-category combo")
    @RequestMapping(path = "/api/supervisor/brand/{brandId}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer brandId, @RequestBody BrandForm brandForm) throws ApiException {
        brandDto.updateBrand(brandId, brandForm);
    }

    @ApiOperation(value = "Getting details of all the brand-category")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

}
