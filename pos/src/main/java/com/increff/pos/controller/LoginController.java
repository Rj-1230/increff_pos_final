package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.UserApi;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserApi service;
    @Autowired
    private InfoData info;

    @Value("${supervisorEmail}")
    private String supervisorEmail;
    @ApiOperation(value = "Logs in a user")
    @RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest request, LoginForm f) throws ApiException {
        UserPojo userPojo = service.getByEmail(f.getEmail());
        boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return new ModelAndView("redirect:/site/login");
        }

        Authentication authentication = convert(userPojo);
        HttpSession session = request.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
        return new ModelAndView("redirect:/ui/home");

    }

    @RequestMapping(path = "/session/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    private Authentication convert(UserPojo p) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(p.getEmail());
        principal.setId(p.getId());

        String role="operator";
        if(Objects.equals(supervisorEmail,p.getEmail())){
            role ="supervisor";
        }
        principal.setRole(role);
        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

}



