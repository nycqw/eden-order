package com.eden.order.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter @Getter
public class TOrder implements Serializable{
    private Long id;

    private Long orderId;

    private Integer orderStatus;

    private Long userId;

    private Long serialNumber;

    private Long productId;

    private Double price;

    private Integer purchaseAmount;

    private String mailingAddress;

    private Date createTime;

    private Date updateTime;
}