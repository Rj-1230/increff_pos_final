package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import com.increff.pos.service.AboutAppService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	@Value("${hibernate.dialect}")
	private String abc;

	@Test
	public void testServiceApis() {
		assertEquals("Point of Sale Application", service.getName());
		assertEquals("1.0", service.getVersion());
	}

}