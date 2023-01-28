package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.OrderData;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {
	@Autowired
	private InfoData info;

	@Value("${app.baseUrl}")
	private String baseUrl;

	protected ModelAndView mav (String page)
	{
		UserPrincipal principal = SecurityUtil.getPrincipal();
		info.setEmail(principal == null ? "" : principal.getEmail());
		info.setRole(principal == null ? "" : principal.getRole());

		ModelAndView mav = new ModelAndView(page);

		mav.addObject("info", info);
		mav.addObject("role", info.getRole());
		mav.addObject("counterId", principal == null ? 0 : principal.getId());
		mav.addObject("baseUrl", baseUrl);
		return mav;

	}


	protected ModelAndView mav (String page, OrderData orderData)
	{
		// Get current user
		UserPrincipal principal = SecurityUtil.getPrincipal();
        info.setEmail(principal == null ? "" : principal.getEmail());
		info.setRole(principal == null ? "" : principal.getRole());
		ModelAndView mav = new ModelAndView(page);

		mav.addObject("info", info);
		mav.addObject("orderId", orderData.getOrderId());
		mav.addObject("counterId", principal == null ? 0 : principal.getId());
		mav.addObject("customerName", orderData.getCustomerName());
		mav.addObject("status", orderData.getStatus());
		mav.addObject("baseUrl", baseUrl);
		return mav;
	}

}