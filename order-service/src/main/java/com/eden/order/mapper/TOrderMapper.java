package com.eden.order.mapper;

import com.eden.order.model.TOrder;

import java.util.List;

public interface TOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TOrder record);

    int insertSelective(TOrder record);

    TOrder selectByPrimaryKey(Long id);

    List<TOrder> selectTimeoutOrder();

    int updateByPrimaryKeySelective(TOrder record);

    int updateByPrimaryKey(TOrder record);
}