package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api

public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Adding or removing a product's certain quantity from the inventory")
    @RequestMapping(path = "/api/supervisor/inventoryAddSub", method = RequestMethod.POST)
    public void update(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.updateInventory(inventoryForm);
    }

    @ApiOperation(value = "Getting details of a product in the Inventory")
    @RequestMapping(path = "/api/inventory/{inventoryId}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer inventoryId) throws ApiException {
        return inventoryDto.get(inventoryId);
    }

    @ApiOperation(value = "Getting details of all the Inventory items")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAll();
    }

}
