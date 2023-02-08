package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.InventoryDtoHelper.convert;
import static com.increff.pos.helper.dtoHelper.InventoryDtoHelper.normalize;
import static com.increff.pos.util.ValidateFormUtil.validateForm;

@Service

public class InventoryDto {
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InventoryFlow inventoryFlow;


    public void updateInventory(InventoryForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        inventoryFlow.update(convert(f), f.getBarcode());
    }

    public InventoryData get(Integer id) throws ApiException {
        return inventoryFlow.get(id);
    }

    public List<InventoryData> getAll() throws ApiException {
        return inventoryFlow.getAll();
    }


}
