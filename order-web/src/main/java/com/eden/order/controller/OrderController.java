package com.eden.order.controller;

import com.eden.order.param.OrderParam;
import com.eden.order.result.Result;
import com.eden.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenqw
 * @since 2018/11/22
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/create")
    public Result createOrder(@RequestBody OrderParam orderParam) {
        Long orderId = orderService.createOrder(orderParam);
        return Result.success(orderId);
    }

}
