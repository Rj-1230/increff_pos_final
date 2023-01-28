package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Get Revenue on product items with date,brand,category as filter")
    @RequestMapping(path = "/api/revenue-brand-category", method = RequestMethod.POST)
    public List<ProductRevenueData> getRevenueBrandCategoryWise(@RequestBody DateBrandCategoryFilterForm f) throws ApiException
    {
        return reportDto.getRevenueBrandCategoryWise(f);
    }

//    @ApiOperation(value = "Get Inventory filtered by Brand and category")
//    @RequestMapping(path = "/api/inventory-brand-category", method = RequestMethod.POST)
//    public List<InventoryReportData> getInventoryBrandCategoryWise(@RequestBody BrandForm f) throws ApiException
//    {
//        return reportDto.getInventoryBrandCategoryWise(f);
//    }

    @ApiOperation(value = "Get Brand filtered by Brand and category")
    @RequestMapping(path = "/api/brand-category", method = RequestMethod.POST)
    public List<BrandData> getBrandReport(@RequestBody BrandForm f) throws ApiException
    {
        return reportDto.getBrandReport(f);
    }

    @ApiOperation(value = "Get Daily Sales reported filtered by date")
    @RequestMapping(path = "/api/dailyReportFilter", method = RequestMethod.POST)
    public List<DailyReportData> getDailySalesFilteredReport(@RequestBody DateFilterForm f) throws ApiException
    {
        return reportDto.getDailySalesFilteredReport(f);
    }

}