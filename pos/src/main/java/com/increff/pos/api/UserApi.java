package com.increff.pos.api;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class UserApi {

    @Autowired
    private UserDao userDao;

    @Transactional
    public void add(UserPojo userPojo) throws ApiException {
        UserPojo existing = getByEmail(userPojo.getEmail());
        if (existing != null) {
            throw new ApiException("User with given email already exists");
        }
        userDao.insert(userPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public UserPojo getByEmail(String email) throws ApiException {
        return userDao.select(email);
    }

    @Transactional
    public List<UserPojo> getAll() {
        return userDao.selectAll();
    }

    @Transactional
    public void delete(Integer id) throws ApiException {
        userDao.delete(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, UserPojo userPojo) throws ApiException {
        UserPojo ex = getCheck(id);
        ex.setEmail(userPojo.getEmail());
        ex.setPassword(userPojo.getPassword());
    }

    @Transactional(rollbackOn = ApiException.class)
    public  UserPojo getCheck(Integer id) throws ApiException {
        UserPojo userPojo = userDao.select(id);
        if(Objects.isNull(userPojo)){
            throw new ApiException("No such user with given id exists !");
        }
        return userPojo;
    }
}