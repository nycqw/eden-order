package com.eden.order.aspect;

import com.alibaba.fastjson.JSON;
import com.eden.order.aspect.annotation.BusinessLog;
import com.eden.order.domain.model.BusinessLogEntity;
import com.eden.order.mapper.BusinessLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/28
 */
@Aspect
@Component
@Slf4j
public class BusinessLogAspect {

    @Autowired
    private BusinessLogMapper businessLogMapper;

    @Pointcut(value = "@annotation(com.eden.order.aspect.annotation.BusinessLog)")
    public void pointcut(){
    }

    @Around(value = "pointcut() && @annotation(businessLog)")
    public void recordLog(ProceedingJoinPoint joinPoint, BusinessLog businessLog){
        try {
            BusinessLogEntity businessLogEntity = new BusinessLogEntity();
            StringBuilder param = getParam(joinPoint);
            businessLogEntity.setParam(param.toString());
            businessLogEntity.setType(businessLog.type());
            businessLogEntity.setDescription(businessLog.description());
            businessLogEntity.setCreateTime(new Date());
            businessLogMapper.insert(businessLogEntity);

            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                businessLogEntity.setStatus(-1);
            }
            businessLogEntity.setStatus(1);
            businessLogMapper.updateByPrimaryKey(businessLogEntity);
        } catch (Exception e) {
            log.error("业务日志记录异常", e);
        }

    }

    private StringBuilder getParam(JoinPoint joinPoint) {
        StringBuilder param = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            param.append(JSON.toJSONString(arg)).append("\n");
        }
        return param;
    }
}
