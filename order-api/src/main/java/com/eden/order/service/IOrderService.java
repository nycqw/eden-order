package com.eden.order.service;

import com.eden.order.param.OrderParam;

/**
 * Created by 18060757 on 2018/11/22.
 */
public interface IOrderService {

    Long createOrder(OrderParam orderParam);

    void cancelOrder(OrderParam orderParam);

    void confirmOrder(OrderParam orderParam);
}
