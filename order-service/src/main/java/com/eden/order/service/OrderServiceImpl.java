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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenqw
 * @since 2018/11/22
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private TOrderMapper orderMapper;

    /**
     * 异步订单创建为满足重试机制需进行幂等设计
     *
     * @param orderParam
     * @return
     */
    @Override
    public int createOrder(OrderParam orderParam) {
        TOrder orderInfo = orderMapper.selectByPrimaryKey(orderParam.getOrderId());
        if (orderInfo != null) {
            // 幂等设计，避免重试时重复创建
            return 1;
        }
        orderInfo = new TOrder();
        BeanUtils.copyProperties(orderParam, orderInfo);
        orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
        orderInfo.setCreateTime(new Date());
        return orderMapper.insert(orderInfo);
    }
}
