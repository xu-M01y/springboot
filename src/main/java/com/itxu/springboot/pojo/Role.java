package com.itxu.springboot.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * 
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class Role  {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String value;

}