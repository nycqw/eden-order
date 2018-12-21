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

    String ORDER_CANCEL_EXCHANGE = "CREATE_CANCEL_EXCHANGE";
    String ORDER_CANCEL_QUEUE = "topic.order.cancel";
    String ORDER_CANCEL_KEY = "topic.order.cancel";

    String ORDER_CONFIRM_EXCHANGE = "CREATE_CONFIRM_EXCHANGE";
    String ORDER_CONFIRM_QUEUE = "topic.order.confirm";
    String ORDER_CONFIRM_KEY = "topic.order.confirm";

    String ORDER_CREATE_CALLBACK_EXCHANGE = "CREATE_ORDER_CALLBACK_EXCHANGE";
    String ORDER_CREATE_CALLBACK_QUEUE = "topic.order.create.callback";
    String ORDER_CREATE_CALLBACK_KEY = "topic.order.create.callback";
}
