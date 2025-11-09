package com.shop.exception;

/**
 * 自定义异常类：资源未找到
 * 
 * 作用：
 * 1. 当查询数据库或服务时，如果某个资源不存在（如订单、商品、用户等），可以抛出此异常。
 * 2. 可被全局异常处理器捕获，返回 404 状态码给前端，提示资源不存在。
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常提示信息，例如 "订单不存在"
     */
    public ResourceNotFoundException(String message) {
        super(message); // 调用父类 RuntimeException 构造函数
    }
}
