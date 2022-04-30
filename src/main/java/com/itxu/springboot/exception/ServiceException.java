package com.itxu.springboot.exception;

/**
 * @author show
 * @create 2022-03-26-13:47
 */

import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException{

    private String code;


    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }

}
