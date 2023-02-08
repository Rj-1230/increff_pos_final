package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.DailyReportApi;
import com.increff.pos.flow.ReportFlow;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.ProductRevenueData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.DateBrandCategoryFilterForm;
import com.increff.pos.model.form.DateFilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.BrandDtoHelper.normalize;
import static com.increff.pos.helper.dtoHelper.ReportDtoHelper.normalize;
import static com.increff.pos.util.ValidateFormUtil.checkDate;
import static com.increff.pos.util.ValidateFormUtil.validateForm;

@Service
public class ReportDto {
    @Autowired
    ReportFlow reportFlow;

    @Autowired
    DailyReportApi dailyReportApi;


    public List<ProductRevenueData> getRevenueReport(DateBrandCategoryFilterForm form) throws ApiException {
        validateForm(form);
        checkDate(form.getStart(), form.getEnd());
        normalize(form);
        return reportFlow.getRevenueReport(form);
    }

    public List<InventoryReportData> getInventoryReport(BrandForm form) throws ApiException {
        validateForm(form);
        normalize(form);
        return reportFlow.getInventoryReport(form);
    }

    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
        validateForm(form);
        normalize(form);
        return reportFlow.getBrandReport(form);
    }

    public List<DailyReportData> getDailySalesFilteredReport(DateFilterForm form) throws ApiException {
        validateForm(form);
        checkDate(form.getStart(), form.getEnd());
        return reportFlow.getDailySalesFilteredReport(form);
    }

    public void createDailyReport() throws ApiException {
        reportFlow.createDailyReport();
    }


}