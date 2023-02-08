package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

    @Autowired
    OrderDto orderDto;

    @RequestMapping(value = "/ui/home")
    public ModelAndView home() {
        return mav("home.html");
    }

    @RequestMapping(value = "/ui/brands")
    public ModelAndView brands() {
        return mav("brands.html");
    }

    @RequestMapping(value = "/ui/products")
    public ModelAndView products() {
        return mav("products.html");
    }

    @RequestMapping(value = "/ui/inventory")
    public ModelAndView inventory() {
        return mav("inventory.html");
    }

    @RequestMapping(value = "/ui/orders")
    public ModelAndView orders() {
        return mav("orders.html");
    }

    @RequestMapping(value = "/ui/revenue")
    public ModelAndView revenue() {
        return mav("revenue.html");
    }

    @RequestMapping(value = "/ui/inventoryReport")
    public ModelAndView inventoryReport() {
        return mav("inventoryReport.html");
    }

    @RequestMapping(value = "/ui/daily_sales")
    public ModelAndView DailySalesReport() {
        return mav("daily_sales.html");
    }

    @RequestMapping(value = "/ui/brandReport")
    public ModelAndView brandReport() {
        return mav("brandReport.html");
    }

    @RequestMapping(value = "/ui/supervisor/user")
    public ModelAndView user() {
        return mav("user.html");
    }

    @RequestMapping(value = "/ui/supervisor/allOrders")
    public ModelAndView allorders() {
        return mav("allOrders.html");
    }

    @RequestMapping(value = "/ui/orderItem/{orderCode}")
    public ModelAndView orderItemByOrderCode(@PathVariable String orderCode) throws ApiException {
        Integer orderId = orderDto.getOrderByOrderCode(orderCode).getOrderId();
        return mav("orderItem.html", orderId);
    }

}