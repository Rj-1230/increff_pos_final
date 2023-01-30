package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.service.ApiException;
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

    @ApiOperation(value="Adding or removing a product's certain quantity from the inventory")
    @RequestMapping(path="/api/supervisor/inventoryAddSub", method = RequestMethod.POST)
    public void update(@RequestBody InventoryForm f) throws ApiException{
        inventoryDto.updateInventory(f);
    }
    @ApiOperation(value="Deleting a product from Inventory")
    @RequestMapping(path="/api/supervisor/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) throws ApiException{
        inventoryDto.delete(id);
    }

    @ApiOperation(value="Getting details of a product in the Inventory")
    @RequestMapping(path="/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
       return inventoryDto.get(id);
    }

    @ApiOperation(value="Getting details of all the Inventory items")
    @RequestMapping(path="/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException{
        return inventoryDto.getAll();
    }

}
