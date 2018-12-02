package com.eden.order.producer;

import com.alibaba.fastjson.JSON;
import com.eden.order.constants.MQConstants;
import com.eden.order.result.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/24
 */
@Component
public class OrderCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void OrderCreateCallback(Result result){
        String message = JSON.toJSONString(result);
        rabbitTemplate.convertAndSend(MQConstants.ORDER_CREATE_CALLBACK_EXCHANGE, MQConstants.ORDER_CREATE_CALLBACK_KEY, message);
    }
}
