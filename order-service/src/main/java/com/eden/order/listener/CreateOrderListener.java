package com.eden.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.eden.order.domain.OrderParam;
import com.eden.order.enums.OrderStatusEnum;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.eden.order.utils.SnowFlakeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/18
 */
@Component
@Slf4j
public class CreateOrderListener {

    @Autowired
    private TOrderMapper orderMapper;

    @RabbitListener(bindings = @QueueBinding(
            key = RabbitConstants.ORDER_CREATE_KEY,
            value = @Queue(value = RabbitConstants.ORDER_CREATE_QUEUE),
            exchange = @Exchange(value = RabbitConstants.ORDER_CREATE_EXCHANGE)
    ))
    public void createOrder(String message) {
        log.info("监听成功... message:{}==========================", message);
        OrderParam orderParam = JSON.parseObject(message, new TypeReference<OrderParam>() {});

        TOrder orderInfo = new TOrder();
        orderInfo.setUserId(orderParam.getUserId());
        orderInfo.setProductId(orderParam.getProductId());
        orderInfo.setCreateTime(new Date());
        orderInfo.setOrderId(SnowFlakeGenerator.nextId());
        orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
        orderInfo.setPurchaseAmount(orderParam.getNumber());
        orderMapper.insert(orderInfo);
    }
}
