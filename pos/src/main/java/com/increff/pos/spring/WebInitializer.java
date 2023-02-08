package com.increff.pos.spring;

//
// Note :- This is a draft file which will always be used as WebInitializer.java inside the spring package

//


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is a hook for <b>Servlet 3.0</b> specification, to initialize
 * Spring configuration without any <code>web.xml</code> configuration. Note
 * that {@link #getServletConfigClasses} method returns {@link SpringConfig},
 * which is the starting point for Spring configuration <br>
 * <b>Note:</b> You can also implement the {@link WebApplicationInitializer }
 * interface for more control
 */

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

//    This basically tells  to Tomcat/Jetty that I will be using SpringConfig class to initiate my spring and load the basic Spring framework.
//    Thus, my spring gets configured or gets initiated/initialized.

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}