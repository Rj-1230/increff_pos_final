package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public void add(UserPojo p) throws ApiException {
        UserPojo existing = getByEmail(p.getEmail());
        if (existing != null) {
            throw new ApiException("User with given email already exists");
        }
        userDao.insert(p);
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
    public void delete(int id) {
        userDao.delete(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, UserPojo p) throws ApiException {
        UserPojo ex = getCheck(id);
        ex.setEmail(p.getEmail());
        ex.setPassword(p.getPassword());
    }

    @Transactional(rollbackOn = ApiException.class)
    public  UserPojo getCheck(int id) throws ApiException {
        UserPojo a = userDao.select(id);
        if(!Objects.nonNull(a)){
            throw new ApiException("No such user with given id exists !");
        }
        return a;
    }
}