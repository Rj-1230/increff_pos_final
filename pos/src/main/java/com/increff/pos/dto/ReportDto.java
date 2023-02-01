package com.increff.pos.dto;

import com.increff.pos.flow.ReportFlow;
import com.increff.pos.model.*;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.increff.pos.helper.NullCheckHelper.checkDate;
import static com.increff.pos.helper.ReportFlowHelper.convert;

@Service
public class ReportDto {
    @Autowired
    ReportFlow reportFlow;

    @Autowired
    ReportApi reportApi;


    public List<ProductRevenueData> getRevenueBrandCategoryWise(DateBrandCategoryFilterForm form) throws ApiException {
        return reportFlow.getRevenueBrandCategoryWise(form);
    }

    public List<InventoryReportData> getInventoryBrandCategoryWise(BrandForm form) throws ApiException {
        return reportFlow.getInventoryBrandCategoryWise(form);
    }

    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
       return reportFlow.getBrandReport(form);
    }

    public List<DailyReportData> getDailySalesFilteredReport(DateFilterForm form) throws ApiException
    {
        checkDate(form);
       return reportFlow.getDailySalesFilteredReport(form);
    }

    public void createDailyReport() throws ApiException {
        reportFlow.createDailyReport();
    }


}