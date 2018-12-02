package com.eden.order.aspect;

import com.eden.order.result.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenqw
 * @version 1.0
 * @since 2018/11/28
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 未知异常处理
     *
     * @param e 未知异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandle(Exception e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 参数校验异常处理
     *
     * @param e 参数校验异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result argumentValidExceptionHandle(MethodArgumentNotValidException e) {
        FieldError fieldError = (FieldError) e.getBindingResult().getAllErrors().get(0);
        return Result.fail(fieldError.getDefaultMessage());
    }
}
