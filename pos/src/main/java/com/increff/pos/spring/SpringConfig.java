package com.increff.pos.spring;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@Configuration
@EnableWebMvc
@ComponentScan("com.increff.pos")
@PropertySources({
        @PropertySource(value="file:./pos.properties", ignoreResourceNotFound = true )
})
//This will enable my Controller and Rest controller annotated classes

public class SpringConfig {

}
