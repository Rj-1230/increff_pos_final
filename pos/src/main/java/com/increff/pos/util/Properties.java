package com.increff.pos.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Properties {
    @Value("${invoice_app.url}")
    private String invoice_appUrl;
    @Value("${app.baseUrl}")
    private String baseUrl;
    @Value("${supervisorEmail}")
    private String supervisorEmail;

}
