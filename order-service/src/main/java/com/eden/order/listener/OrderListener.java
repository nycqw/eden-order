package com.eden.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.eden.order.constants.MQConstants;
import com.eden.order.constants.OrderStatusEnum;
import com.eden.order.mapper.TOrderMapper;
import com.eden.order.model.TOrder;
import com.eden.order.param.OrderParam;
import com.eden.order.producer.OrderCallback;
import com.eden.order.result.Result;
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
    private TOrderMapper orderMapper;

    @Autowired
    private OrderCallback orderCallback;

    /**
     * 订单创建
     *
     * @param msg
     * @param channel
     * @param message
     */
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
            //createOrder(orderParam);
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

    /**
     * 订单取消
     *
     * @param msg
     * @param channel
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            key = MQConstants.ORDER_CANCEL_KEY,
            value = @Queue(value = MQConstants.ORDER_CANCEL_QUEUE, durable = "true"),
            exchange = @Exchange(value = MQConstants.ORDER_CANCEL_EXCHANGE, type = "topic")
    ))
    public void cancelOrder(String msg, Channel channel, Message message) {
        OrderParam orderParam = JSON.parseObject(msg, new TypeReference<OrderParam>() {
        });
        try {
            log.info("监听成功, 消息内容:{}", msg);
            TOrder orderInfo = orderMapper.selectByPrimaryKey(orderParam.getOrderId());
            orderInfo.setOrderStatus(OrderStatusEnum.CANCELED_STATUS.getStatus());
            orderMapper.updateByPrimaryKey(orderInfo);
            log.info("消费成功，从队列中删除");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                log.error("消费失败，返回失败", e);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                log.error("否定应答失败", ex);
            }
        }
    }

    /**
     * 订单确认
     *
     * @param msg
     * @param channel
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            key = MQConstants.ORDER_CONFIRM_KEY,
            value = @Queue(value = MQConstants.ORDER_CONFIRM_QUEUE, durable = "true"),
            exchange = @Exchange(value = MQConstants.ORDER_CONFIRM_EXCHANGE, type = "topic")
    ))
    public void confirmOrder(String msg, Channel channel, Message message) {
        OrderParam orderParam = JSON.parseObject(msg, new TypeReference<OrderParam>() {
        });
        try {
            log.info("监听成功, 消息内容:{}", msg);
            TOrder orderInfo = orderMapper.selectByPrimaryKey(orderParam.getOrderId());
            orderInfo.setOrderStatus(OrderStatusEnum.COMPLETED_STATUS.getStatus());
            orderMapper.updateByPrimaryKey(orderInfo);
            log.info("消费成功，从队列中删除");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                log.error("消费失败，返回失败", e);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                log.error("否定应答失败", ex);
            }
        }
    }

    /*private void createOrder(OrderParam orderParam) {
        TOrder orderInfo = orderMapper.selectByPrimaryKey(orderParam.getOrderId());
        // 幂等设计，避免重试时重复创建
        if (orderInfo == null) {
            orderInfo = new TOrder();
            BeanUtils.copyProperties(orderParam, orderInfo);
            orderInfo.setOrderStatus(OrderStatusEnum.CREATED_STATUS.getStatus());
            orderInfo.setCreateTime(new Date());
            orderMapper.insert(orderInfo);
        }
    }*/

}
