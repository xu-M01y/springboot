package com.itxu.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itxu.springboot.exception.ServiceException;
import com.itxu.springboot.common.Constants;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.mapper.RoleMapper;
import com.itxu.springboot.mapper.RoleMenuMapper;
import com.itxu.springboot.pojo.Menu;
import com.itxu.springboot.pojo.dto.UserDto;
import com.itxu.springboot.mapper.UserMapper;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.pojo.dto.UserPasswordDTO;
import com.itxu.springboot.service.IMenuService;
import com.itxu.springboot.service.UserService;
import com.itxu.springboot.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author show
 * @create 2022-03-22-21:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private IMenuService iMenuService;
    @Autowired
    private UserMapper userMapper;


    @Override
    public boolean saveUser(User user) {

        return saveOrUpdate(user);
    }

    public UserDto login(UserDto userDto) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .eq("username",userDto.getUsername())
                .eq("password",userDto.getPassword());
        User one = getOne(userQueryWrapper);
        if (one != null){
            BeanUtil.copyProperties(one,userDto,true);
            //设置token
            String token = JwtUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);

            String role = one.getRole();    //ROLE_ADMIN

            List<Menu> roleMenus = getRoleMenus(role);
          //  Collections.reverse(roleMenus);    // 实现list集合逆序排列
            //设置用户的菜单列表
            userDto.setMenu(roleMenus);
            return userDto;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名密码错误");
        }

    }

    @Override
    public Result register(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (getOne(userQueryWrapper.eq("username",user.getUsername())) ==null){

            saveUser(user);
            return Result.success();
        }
            return Result.error(Constants.CODE_600,"用户名已存在");
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    @Override
    public Page<User> findPage(Page<User> page, String username, String email, String address) {

        return userMapper.findPage(page,username,email,address);
    }

    private List<Menu> getRoleMenus(String roleValue){
        Integer roleId = roleMapper.selectByValue(roleValue);
        //当前角色所有的菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        //查出系统所有菜单
        List<Menu> menus = iMenuService.findMenus(null);
        // new一个最后筛选完成的list集合
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf 移除childre 里面不在menuids集合里面的 元素
            children.removeIf(child -> ! menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}