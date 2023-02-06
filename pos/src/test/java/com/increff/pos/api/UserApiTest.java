package com.increff.pos.api;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserApiTest extends AbstractUnitTest {
    @Autowired
    private UserApi userApi;
    @Autowired
    private UserDao userDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddUser() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        UserPojo userPojo = PojoUtil.getUserPojo(email, password);
        userApi.add(userPojo);

        List<UserPojo> userPojoList =  userApi.getAll();
        assertEquals(userPojoList.size(),1);
        assertEquals(userPojoList.get(0).getEmail(),email);
        assertEquals(userPojoList.get(0).getPassword(),password);
    }

    @Test
    public void testSizeOfUserList() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        for(int i=0;i<4;i++){
            UserPojo userPojo = PojoUtil.getUserPojo(email+i,password+i);
            userDao.insert(userPojo);
        }
        List<UserPojo> userPojoList =  userApi.getAll();
        assertEquals(userPojoList.size(),4);
    }

    @Test
    public void testDeleteUser() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        for(int i=0;i<4;i++){
            UserPojo userPojo = PojoUtil.getUserPojo(email+i,password+i);
            userDao.insert(userPojo);
        }
        List<UserPojo> userPojoList =  userApi.getAll();
        assertEquals(userPojoList.size(),4);
         userApi.delete(userPojoList.get(0).getUserId());
        assertEquals(userApi.getAll().size(),3);
    }

    @Test
    public void testUpdateUser() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        UserPojo userPojo = PojoUtil.getUserPojo(email,password);
        userDao.insert(userPojo);

        String updatedEmail = "user";
        String updatedPassword = "password";
        UserPojo updatedUserPojo = PojoUtil.getUserPojo(updatedEmail,updatedPassword);
        userApi.update(userPojo.getUserId(), updatedUserPojo);
        List<UserPojo> userPojoList =  userApi.getAll();
        assertEquals(userPojoList.get(0).getEmail(),updatedEmail);
        assertEquals(userPojoList.get(0).getPassword(),updatedPassword);
    }

    @Test
    public void testGetCheckUser() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        UserPojo userPojo = PojoUtil.getUserPojo(email,password);
        userDao.insert(userPojo);

       UserPojo userPojo1 = userApi.getCheckUser(userPojo.getUserId());
        assertEquals(userPojo1,userPojo);
    }


    @Test
    public void testGetByEmail() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        UserPojo userPojo = PojoUtil.getUserPojo(email,password);
        userDao.insert(userPojo);

        UserPojo userPojo1 = userApi.getUserPojoByEmail(email);
        assertEquals(userPojo1,userPojo);
    }


@Test
    public void testUserUniquenessByEmail() throws ApiException {
    String email= "testEmail";
    String password ="testPassword";
    UserPojo userPojo = PojoUtil.getUserPojo(email,password);
    userDao.insert(userPojo);

    String newPassword ="Password";
    UserPojo newUserPojo = PojoUtil.getUserPojo(email,newPassword);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("User with given email already exists");
        userApi.add(newUserPojo);
    }

    @Test
    public void testUserExistence() throws ApiException {
        String email= "testEmail";
        String password ="testPassword";
        UserPojo userPojo = PojoUtil.getUserPojo(email,password);
        userDao.insert(userPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such user with given id exists !");
        userApi.getCheckUser(userPojo.getUserId()+1);
    }
}
