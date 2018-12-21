package com.eden.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.eden.order.aspect.annotation.BusinessLog;
import com.eden.order.constants.MQConstants;
import com.eden.order.constants.OrderStatusEnum;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.eden.order.param.OrderParam;
import com.eden.order.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author chenqw
 * @since 2018/11/22
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 异步订单创建为满足重试机制需进行幂等设计
     *
     * @param orderParam
     * @return
     */
    @Override
    @BusinessLog(description = "订单创建")
    public Long createOrder(OrderParam orderParam) {
        TOrder orderInfo = orderMapper.selectByPrimaryKey(orderParam.getOrderId());
        // 幂等设计，避免重试时重复创建
        if (orderInfo == null) {
            orderInfo = new TOrder();
            long orderId = SnowFlake.generatingId();
            BeanUtils.copyProperties(orderParam, orderInfo);
            orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
            orderInfo.setCreateTime(new Date());
            orderInfo.setOrderId(orderId);
            orderMapper.insert(orderInfo);
            return orderId;
        }
        /*CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(orderId));
        String textMessage = JSON.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_CREATE_EXCHANGE, MQConstants.ORDER_CREATE_KEY, textMessage, correlationData);*/
        return orderInfo.getOrderId();
    }

    @BusinessLog(description = "订单取消")
    @Override
    public void cancelOrder(OrderParam orderParam) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(orderParam.getOrderId()));
        String textMessage = JSON.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_CANCEL_EXCHANGE, MQConstants.ORDER_CANCEL_KEY, textMessage, correlationData);
    }

    @BusinessLog(description = "订单完工")
    @Override
    public void confirmOrder(OrderParam orderParam) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(orderParam.getOrderId()));
        String textMessage = JSON.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_CONFIRM_EXCHANGE, MQConstants.ORDER_CONFIRM_KEY, textMessage, correlationData);
    }
}
