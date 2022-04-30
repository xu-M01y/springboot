package com.itxu.springboot.service;

import com.itxu.springboot.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itxu
 * @since 2022-03-28
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
