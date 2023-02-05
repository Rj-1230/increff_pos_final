package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class UserController {

    @Autowired
    private UserDto userDto;

    @ApiOperation(value="Adding new operator")
    @RequestMapping(path="/api/supervisor/user", method = RequestMethod.POST)
    public void add(@RequestBody UserForm f)throws ApiException {
        userDto.addUser(f);
    }

    @ApiOperation(value="Deleting a operator")
    @RequestMapping(path="/api/supervisor/user/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) throws ApiException {
        userDto.deleteUser(id);
    }

    @ApiOperation(value="Getting details of a operator")
    @RequestMapping(path="/api/supervisor/user/{id}", method = RequestMethod.GET)
    public UserData get(@PathVariable Integer id) throws ApiException {
        return userDto.getUser(id);
    }

    @ApiOperation(value="Updating details of a particular operator")
    @RequestMapping(path="/api/supervisor/user/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody UserForm f) throws ApiException {
        userDto.updateUser(id,f);
    }

    @ApiOperation(value="Getting details of all the operators")
    @RequestMapping(path="/api/supervisor/user", method = RequestMethod.GET)
    public List<UserData> getAll() {
        return userDto.getAllUsers();
    }
}
