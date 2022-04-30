package com.itxu.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author show
 * @create 2022-03-26-13:14
 */

//接口同意包装类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result <T>{




    private String code;
    private String msg;
    private Object data;

    public static Result  success(){
        return new Result(Constants.CODE_200,"",null);
    }

    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }

    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
}
