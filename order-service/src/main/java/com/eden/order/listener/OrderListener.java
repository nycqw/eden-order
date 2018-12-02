package com.eden.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.eden.order.constants.MQConstants;
import com.eden.order.param.OrderParam;
import com.eden.order.result.Result;
import com.eden.order.service.IOrderService;
import com.eden.order.producer.OrderCallback;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/18
 */
@Component
@Slf4j
public class OrderListener {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderCallback orderCallback;

    @RabbitListener(bindings = @QueueBinding(
            key = MQConstants.ORDER_CREATE_KEY,
            value = @Queue(value = MQConstants.ORDER_CREATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = MQConstants.ORDER_CREATE_EXCHANGE, type = "topic")
    ))
    public void createOrder(String msg, Channel channel, Message message) {
        OrderParam orderParam = JSON.parseObject(msg, new TypeReference<OrderParam>() {
        });
        try {
            log.info("监听成功, 消息内容:{}", msg);
            orderService.createOrder(orderParam);
            log.info("消费成功，从队列中删除");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            orderCallback.OrderCreateCallback(Result.success(orderParam.getOrderId()));

        } catch (Exception e) {
            try {
                log.error("消费失败，返回失败", e);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                orderCallback.OrderCreateCallback(Result.fail(e.getMessage(), orderParam.getOrderId()));
            } catch (Exception ex) {
                log.error("否定应答失败", ex);
            }
        }
    }

}
