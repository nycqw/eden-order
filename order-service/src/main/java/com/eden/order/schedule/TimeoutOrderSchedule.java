package com.eden.order.schedule;

import com.alibaba.fastjson.JSON;
import com.eden.order.constants.MQConstants;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.eden.order.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/12/22
 */
@Component
@Slf4j
public class TimeoutOrderSchedule {

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void schedule(){
        List<TOrder> orderList = orderMapper.selectTimeoutOrder();
        for (TOrder order : orderList) {
            OrderParam orderParam = new OrderParam();
            BeanUtils.copyProperties(order, orderParam);
            String textMessage = JSON.toJSONString(orderParam);

            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(String.valueOf(order.getOrderId()));

            rabbitTemplate.convertAndSend(MQConstants.ORDER_CANCEL_EXCHANGE, MQConstants.ORDER_CANCEL_KEY, textMessage, correlationData);

        }
    }
}
