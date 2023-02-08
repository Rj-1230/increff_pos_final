package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.util.FormUtil;
import com.increff.pos.util.UserPrincipal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import static com.increff.pos.util.DummyForm.*;
import static org.junit.Assert.assertEquals;

public class UserControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private UserController userController;

    @Before
    public void setup() {
        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }



    @Test
    public void testAddUser() throws ApiException {
        UserForm userForm = getDummyUserForm();
        userController.add(userForm);

        List<UserData> userDataList = userController.getAll();
        assertEquals(1, userDataList.size());
        assertEquals("abcd@gmail.com", userDataList.get(0).getEmail());
        assertEquals("abcd@1234", userDataList.get(0).getPassword());
    }

    @Test
    public void testGetUser() throws ApiException {
        UserForm userForm = getDummyUserForm();
        userController.add(userForm);

        UserData userData = userController.getAll().get(0);
        UserData userData1 = userController.get(userData.getUserId());
        assertEquals(userData1.getUserId(), userData.getUserId());
        assertEquals(userData1.getEmail(), userData.getEmail());
        assertEquals(userData1.getPassword(), userData.getPassword());
    }

    @Test
    public void testUpdateUser() throws ApiException {
        UserForm userForm = getDummyUserForm();
        userController.add(userForm);

        UserForm updatedUserForm = FormUtil.getUserForm("abcd@gmail.com", "test_password");
        UserData userData = userController.getAll().get(0);
        userController.update(userData.getUserId(), updatedUserForm);
        UserData userData1 = userController.get(userData.getUserId());
        assertEquals("abcd@gmail.com", userData1.getEmail());
        assertEquals("test_password", userData1.getPassword());
    }

    @Test
    public void testSizeOfUserList() throws ApiException {
        UserForm userForm = getDummyUserForm();
        userController.add(userForm);

        UserForm newUserForm = FormUtil.getUserForm("abcde@gmail.com", "test_password");
        userController.add(newUserForm);
        assertEquals(2, userController.getAll().size());
    }

    @Test
    public void testEmailNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The email field can't be null");
        userController.add(FormUtil.getUserForm(null,"password@123"));
    }
    @Test
    public void testPasswordNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The password field can't be null");
        userController.add(FormUtil.getUserForm("aashish@gmail.com",null));
    }

    @Test
    public void testEmailNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Email can't be empty");
        userController.add(FormUtil.getUserForm("   ","password@123"));
    }
    @Test
    public void testPasswordNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The password can't be empty");
        userController.add(FormUtil.getUserForm("aashish@gmail.com","   "));
    }

    @Test
    public void testPasswordMinSize() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The password must be at least 8 characters long");
        userController.add(FormUtil.getUserForm("aashish@gmail.com","abcd"));
    }
    @Test
    public void testPasswordMaxSize() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The password can be max 15 characters long");
        userController.add(FormUtil.getUserForm("aashish@gmail.com","abcdefgh12345678"));
    }
}
