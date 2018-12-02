package com.eden.order.mapper;

import com.eden.order.domain.model.BusinessLogEntity;

public interface BusinessLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessLogEntity record);

    int insertSelective(BusinessLogEntity record);

    BusinessLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessLogEntity record);

    int updateByPrimaryKey(BusinessLogEntity record);
}