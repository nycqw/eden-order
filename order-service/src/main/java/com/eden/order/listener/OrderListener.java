package com.eden.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.eden.order.constants.OrderStatusEnum;
import com.eden.order.param.OrderParam;
import com.eden.order.constants.MQConstants;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/18
 */
@Component
@Slf4j
public class OrderListener {

    @Autowired
    private TOrderMapper orderMapper;

    @RabbitListener(bindings = @QueueBinding(
            key = MQConstants.ORDER_CREATE_KEY,
            value = @Queue(value = MQConstants.ORDER_CREATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = MQConstants.ORDER_CREATE_EXCHANGE, type = "topic")
    ))
    public void createOrder(String msg, Channel channel, Message message) {
        try {
            log.info("listener success, message:{}", message);
            OrderParam orderParam = JSON.parseObject(msg, new TypeReference<OrderParam>() {});
            TOrder orderInfo = new TOrder();
            BeanUtils.copyProperties(orderParam, orderInfo);
            orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
            orderInfo.setCreateTime(new Date());
            orderMapper.insert(orderInfo);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消费成功，可以从队列中删除");
        } catch (IOException e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                log.error("消费失败，丢弃消息",e);
            } catch (IOException e1) {
                log.error("丢弃失败",e1);
            }
        }

    }
}
