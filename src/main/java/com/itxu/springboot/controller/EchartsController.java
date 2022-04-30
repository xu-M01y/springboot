package com.itxu.springboot.controller;

/**
 * @author show
 * @create 2022-03-27-21:03
 */

import cn.hutool.core.collection.CollUtil;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.mapper.UserMapper;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }



    @GetMapping("/members")
    public Result getDb(){
        List<User> users = userMapper.selectList(null);
        List<Integer> ids = new ArrayList<>();
        for ( User user:users) {
            Integer id = user.getId();
            ids.add(id);
        }
        return Result.success(ids);
    }


}
