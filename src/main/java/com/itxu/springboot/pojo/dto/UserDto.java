package com.itxu.springboot.pojo.dto;

import com.itxu.springboot.pojo.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author show
 * @create 2022-03-26-13:05
 */
@Data
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menu;
}
