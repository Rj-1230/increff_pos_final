package com.increff.invoice_app.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.increff.invoice_app")
@PropertySources({
        @PropertySource(value = "file:./invoice_app.properties", ignoreResourceNotFound = true)
})
//This will enable my Controller and Rest controller annotated classes

public class SpringConfig {

}
