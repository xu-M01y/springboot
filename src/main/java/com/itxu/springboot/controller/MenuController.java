package com.itxu.springboot.controller;

import com.itxu.springboot.common.Constants;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.mapper.DictMapper;
import com.itxu.springboot.pojo.Dict;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.itxu.springboot.service.IMenuService;
import com.itxu.springboot.pojo.Menu;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author itxu
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/menu")
        public class MenuController {
    
@Resource
private IMenuService menuService;
@Resource
private DictMapper dictMapper;

// 新增或者更新
@PostMapping
public boolean save(@RequestBody Menu menu) {
        return menuService.saveOrUpdate(menu);
        }

@DeleteMapping("/{id}")
public Boolean delete(@PathVariable Integer id) {
        return menuService.removeById(id);
        }

        @PostMapping("/del/batch")
        public boolean deleteBatch(@RequestBody List<Integer> ids) {
                return menuService.removeByIds(ids);
        }

        @GetMapping
        public Result findAll(@RequestParam(defaultValue = "") String name) {
                return Result.success(menuService.findMenus(name));
        }
        @GetMapping("/ids")
        public Result findAllIds() {
                return Result.success(menuService.list().stream().map(Menu::getId));
        }

        @GetMapping("/{id}")
        public Menu findOne(@PathVariable Integer id) {
                return menuService.getById(id);
        }


@GetMapping("/icons")
public Result getIcons() {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(dictQueryWrapper));
     }

}

