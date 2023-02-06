package com.increff.pos.dto;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.util.NullCheckHelper.checkNullable;
import static com.increff.pos.helper.dtoHelper.UserDtoHelper.*;
@Service
public class UserDto {
    @Autowired
    UserApi userApi;

    public void addUser(UserForm f) throws ApiException
    {
        checkNullable(f);
        normalize(f);
        userApi.add(convert(f));
    }

    public UserData getUser(Integer id) throws ApiException {
        return convert(userApi.getCheckUser(id));
    }

    public void updateUser(@PathVariable Integer id, @RequestBody UserForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        userApi.update(id,convert(f));
    }

    public List<UserData> getAllUsers(){
        return getAllUserData(userApi.getAll());
    }
}