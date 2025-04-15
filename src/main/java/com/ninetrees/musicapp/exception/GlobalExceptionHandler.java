package com.ninetrees.musicapp.exception;
import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.constant.ResultCode;
import com.ninetrees.musicapp.exception.ChanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Classname:GlobleExceptionHandler
 * Description:
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(){
        return R.error("统一异常处理");
    }

    @ExceptionHandler(ChanException.class)
    @ResponseBody
    public R error(ChanException e) {
        log.error(e.getMessage());
        e.printStackTrace();

        return R.error().code(ResultCode.ERROR).message(e.getMessage());
    }
}
