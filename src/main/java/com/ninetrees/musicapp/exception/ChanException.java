package com.ninetrees.musicapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classname:ChanException
 * Description:自定义异常类
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChanException extends RuntimeException{
    private Integer code;
    private String message;

    public ChanException(String message){
        this.setMessage(message);
    }


}
