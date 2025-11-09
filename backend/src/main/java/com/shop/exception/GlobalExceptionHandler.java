package com.shop.exception;

import com.shop.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 
 * 作用：
 * 1. 捕获应用中抛出的各种异常。
 * 2. 将异常转换为统一的 ResponseDTO 格式返回给前端。
 * 3. 避免前端收到未经处理的异常堆栈信息。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 IllegalArgumentException（常用于参数校验失败）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理方法参数校验异常（@Valid 或 @Validated 触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidation(MethodArgumentNotValidException ex) {
        // 获取第一个校验失败字段及信息
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst().orElse("参数校验失败");
        return new ResponseEntity<>(ResponseDTO.fail(message), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBusiness(BusinessException ex) {
        // 如果异常信息是“未登录”或包含 token 相关信息，返回 401，方便前端区分认证失败
        if ("未登录".equals(ex.getMessage()) || 
            (ex.getMessage() != null && ex.getMessage().contains("token"))) {
            return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        // 其他业务异常返回 400
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneral(Exception ex) {
        // 避免泄露敏感信息，返回统一的服务器错误信息
        return new ResponseEntity<>(ResponseDTO.fail("服务器错误：" + ex.getMessage()), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

