package com.eden.order.constants;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/18
 */
public interface MQConstants {

    String ORDER_CREATE_EXCHANGE = "CREATE_ORDER_EXCHANGE";
    String ORDER_CREATE_QUEUE = "topic.order.create";
    String ORDER_CREATE_KEY = "topic.order.create";
}
