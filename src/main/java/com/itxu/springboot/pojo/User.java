package com.itxu.springboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author show
 * @create 2022-03-22-16:03
 */
@Data
@TableName("sys_user")
@ToString
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    @TableField("creat_time")
    private String creatTime;
    @TableField("avatar_url")
    private String avatarUrl;
    private String role;
    @TableField(exist = false)
    private List<Course> courses;
    @TableField(exist = false)
    private List<Course> stuCourses;


}
