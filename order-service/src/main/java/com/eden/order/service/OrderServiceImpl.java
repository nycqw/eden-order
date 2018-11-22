package com.eden.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.eden.order.constants.OrderStatusEnum;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.eden.order.param.OrderParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author chenqw
 * @since 2018/11/22
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private TOrderMapper orderMapper;

    @Override
    public int createOrder(OrderParam orderParam) {
        TOrder orderInfo = new TOrder();
        BeanUtils.copyProperties(orderParam, orderInfo);
        orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
        orderInfo.setCreateTime(new Date());
        return orderMapper.insert(orderInfo);
    }
}
