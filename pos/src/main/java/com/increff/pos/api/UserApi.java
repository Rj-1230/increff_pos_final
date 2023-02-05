package com.increff.pos.api;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserApi {

    @Autowired
    private UserDao userDao;


    public void add(UserPojo userPojo) throws ApiException {
        UserPojo existing = getUserPojoByEmail(userPojo.getEmail());
        if (existing != null) {
            throw new ApiException("User with given email already exists");
        }
        userDao.insert(userPojo);
    }


    public UserPojo getUserPojoByEmail(String email) throws ApiException {
        return userDao.selectUserByEmail(email);
    }


    public List<UserPojo> getAll() {
        return userDao.selectAll();
    }


    public void delete(Integer id) throws ApiException {
        getCheckUser(id);
        userDao.delete(id);
    }

    public void update(Integer id, UserPojo userPojo) throws ApiException {
        UserPojo ex = getCheckUser(id);
        ex.setEmail(userPojo.getEmail());
        ex.setPassword(userPojo.getPassword());
    }

    public  UserPojo getCheckUser(Integer id) throws ApiException {
        UserPojo userPojo = userDao.selectUserById(id);
        if(Objects.isNull(userPojo)){
            throw new ApiException("No such user with given id exists !");
        }
        return userPojo;
    }
}