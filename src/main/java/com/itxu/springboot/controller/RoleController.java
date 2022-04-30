package com.itxu.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.mapper.RoleMapper;
import com.itxu.springboot.pojo.Role;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.service.RoleService;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;

    //查询所有接口
    @GetMapping()
    public Result findAll(){
        List<Role> roles = roleMapper.selectList(null);
        return Result.success(roles);
    }

    //添加修改接口
    @PostMapping
    public Result saveUser(@RequestBody Role role){
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    //删除接口
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){

         roleService.removeById(id);
         return Result.success();
    }
    //删除所有接口
    @PostMapping("/del/batch")
    public Result deleteBatch (@RequestBody List<Integer> ids){

        roleService.removeByIds(ids);
        return Result.success();
    }

    //分页接口
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") String des){


        Page<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            roleQueryWrapper.like("name", name);
        }
        if (!"".equals(des)) {
            roleQueryWrapper.like("description", des);
        }
        roleQueryWrapper.orderByDesc("id");
        IPage<Role> rolePage = roleService.page(page, roleQueryWrapper);
        return Result.success(rolePage);
    }
    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Role> list = roleService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "用户名");
        writer.addHeaderAlias("description", "信息描述");
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    //导出接口
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Role> roles = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Role role = new Role();
            role.setName(row.get(0).toString());
            role.setDescription(row.get(1).toString());
            roles.add(role);
        }

        roleService.saveBatch(roles);
        return true;
    }



    //关系查询接口
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu (@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){

        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu (@PathVariable Integer roleId){

        return Result.success(roleService.getRoleMenu(roleId));
    }
}
