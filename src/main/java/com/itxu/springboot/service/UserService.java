package com.itxu.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.pojo.dto.UserDto;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.pojo.dto.UserPasswordDTO;
import org.springframework.stereotype.Service;

/**
 * @author show
 * @create 2022-03-22-21:00
 */

public interface UserService extends IService<User> {
    public boolean saveUser(User user);
    public UserDto login(UserDto userDto);

    public Result register(User user);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> objectPage, String username, String email, String address);
}
