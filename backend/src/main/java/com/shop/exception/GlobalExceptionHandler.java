package com.shop.exception;

import com.shop.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst().orElse("参数校验失败");
        return new ResponseEntity<>(ResponseDTO.fail(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBusiness(BusinessException ex) {
        // 对于未登录的业务异常，返回 401 以便前端更好识别
        // 将未登录和无效 token 的情况返回 401，便于前端区分认证失败与参数错误
        if ("未登录".equals(ex.getMessage()) || ex.getMessage() != null && ex.getMessage().contains("token")) {
            return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(ResponseDTO.fail(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneral(Exception ex) {
        return new ResponseEntity<>(ResponseDTO.fail("服务器错误：" + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
