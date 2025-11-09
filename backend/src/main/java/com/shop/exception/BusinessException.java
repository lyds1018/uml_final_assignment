package com.shop.exception;

/**
 * 自定义业务异常类
 * 
 * 作用：
 * 1. 用于在业务逻辑中抛出特定的异常信息，例如库存不足、订单状态不允许操作等。
 * 2. 可以被全局异常处理器捕获，返回统一的错误响应给前端。
 */
public class BusinessException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常提示信息
     */
    public BusinessException(String message) {
        super(message); // 调用父类 RuntimeException 的构造函数
    }
}
