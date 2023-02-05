package com.increff.pos.controller;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.UserApi;
import com.increff.pos.properties.Properties;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

@Controller
public class LoginController {

    @Autowired
    private UserApi userApi;
    @Autowired
    private InfoData info;
    @Autowired
    private Properties properties;

    @ApiOperation(value = "Logs in a user")
    @RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest request, LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userApi.getUserPojoByEmail(loginForm.getEmail());
        boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return new ModelAndView("redirect:/site/login");
        }

        Authentication authentication = convert(userPojo);
        HttpSession session = request.getSession(true);
        SecurityUtil.createContext(session);
        SecurityUtil.setAuthentication(authentication);
        return new ModelAndView("redirect:/ui/home");
    }

    @RequestMapping(path = "/session/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    private Authentication convert(UserPojo userPojo) {
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getUserId());

        String role="operator";
        if(Objects.equals(properties.getSupervisorEmail(),userPojo.getEmail())){
            role ="supervisor";
        }
        principal.setRole(role);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

}



