package com.increff.pos.controller;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.properties.Properties;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {
	@Autowired
	private InfoData info;
	@Autowired
	private Properties properties;

	protected ModelAndView mav (String page)
	{
		UserPrincipal principal = SecurityUtil.getPrincipal();
		info.setEmail(principal == null ? "" : principal.getEmail());
		info.setRole(principal == null ? "" : principal.getRole());

		ModelAndView mav = new ModelAndView(page);

		mav.addObject("info", info);
		mav.addObject("role", info.getRole());
		mav.addObject("message", info.getMessage());
		mav.addObject("counterId", principal == null ? 0 : principal.getId());
		mav.addObject("baseUrl", properties.getBaseUrl());
		return mav;

	}


	protected ModelAndView mav (String page, Integer orderId)
	{
		// Get current user
		UserPrincipal principal = SecurityUtil.getPrincipal();
        info.setEmail(principal == null ? "" : principal.getEmail());
		info.setRole(principal == null ? "" : principal.getRole());
		ModelAndView mav = new ModelAndView(page);

		mav.addObject("info", info);
		mav.addObject("role", info.getRole());
		mav.addObject("orderId", orderId);
		mav.addObject("counterId", principal == null ? 0 : principal.getId());
		mav.addObject("baseUrl", properties.getBaseUrl());
		return mav;
	}

}