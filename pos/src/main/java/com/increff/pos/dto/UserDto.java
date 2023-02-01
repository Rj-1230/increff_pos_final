package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.NullCheckHelper.checkNullable;
import static com.increff.pos.helper.UserDtoHelper.*;
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

    public void deleteUser(Integer id) throws ApiException {
        userApi.delete(id);
    }

    public UserData getUser(Integer id) throws ApiException {
        return convert(userApi.getCheck(id));
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