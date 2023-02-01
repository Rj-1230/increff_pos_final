package com.increff.pos.dto;

import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.increff.pos.helper.InventoryDtoHelper.*;
import static com.increff.pos.helper.NullCheckHelper.checkNullable;

@Service

public class InventoryDto {
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private InventoryFlow inventoryFlow;


    public void updateInventory(InventoryForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        inventoryFlow.update(convert(f));
    }

    public void delete(@PathVariable Integer id){
        inventoryApi.delete(id);
    }

    public InventoryData get(Integer id) throws ApiException {
        return inventoryFlow.get(id);
    }

    public List<InventoryData> getAll() throws ApiException{
        return inventoryFlow.getAll();
    }


}
