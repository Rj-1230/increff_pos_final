package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.ProductRevenueData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.DateBrandCategoryFilterForm;
import com.increff.pos.model.form.DateFilterForm;
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

    @ApiOperation(value = "Get Revenue of invoiced order items with date,brand,category as filter")
    @RequestMapping(path = "/api/report/revenue-brand-category", method = RequestMethod.POST)
    public List<ProductRevenueData> getRevenueReport(@RequestBody DateBrandCategoryFilterForm dateBrandCategoryFilterForm) throws ApiException {
        return reportDto.getRevenueReport(dateBrandCategoryFilterForm);
    }

    @ApiOperation(value = "Get Inventory filtered by Brand and category")
    @RequestMapping(path = "/api/report/inventory-brand-category", method = RequestMethod.POST)
    public List<InventoryReportData> getInventoryReport(@RequestBody BrandForm brandForm) throws ApiException {
        return reportDto.getInventoryReport(brandForm);
    }

    @ApiOperation(value = "Get Brand filtered by Brand and category")
    @RequestMapping(path = "/api/report/brand-category", method = RequestMethod.POST)
    public List<BrandData> getBrandReport(@RequestBody BrandForm brandForm) throws ApiException {
        return reportDto.getBrandReport(brandForm);
    }

    @ApiOperation(value = "Get Daily Sales report filtered by date")
    @RequestMapping(path = "/api/report/dailyReportFilter", method = RequestMethod.POST)
    public List<DailyReportData> getDailySalesReport(@RequestBody DateFilterForm dateFilterForm) throws ApiException {
        return reportDto.getDailySalesFilteredReport(dateFilterForm);
    }

}