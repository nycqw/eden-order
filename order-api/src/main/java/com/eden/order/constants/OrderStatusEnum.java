package com.eden.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/18
 */
@AllArgsConstructor
public enum OrderStatusEnum {

    CREATED_STATUS(0,"订单创建"),
    COMPLETED_STATUS(1,"订单完成"),
    CANCED_STATUS(2,"订单取消"),
    TIMEOUT_STATUS(3,"订单超时");

    @Getter
    private int status;
    @Getter
    private String description;
}
