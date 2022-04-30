package com.itxu.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.common.Constants;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.pojo.dto.UserDto;
import com.itxu.springboot.mapper.UserMapper;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.pojo.dto.UserPasswordDTO;
import com.itxu.springboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author show
 * @create 2022-03-22-16:50
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserServiceImpl userService;


    //查询所有接口
    @GetMapping("/a")
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    //根据角色查询用户
    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",role);
        List<User> list = userService.list(queryWrapper);
        return Result.success(list);
    }

    //插入修改接口
    @PostMapping
    public boolean saveUser(@RequestBody User user) {

        return userService.saveUser(user);
    }

    //删除接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }
    @PostMapping("del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeByIds(ids);
    }



    //分页接口
    //@RequestParam接收 页码 数据
    //limit第一个参数 = (pageNum - 1) * pageSize
    //pageSize
    //通过username来进一步细分 分页
   /* @GetMapping("/page")
    public Map<String,Object> findPageByName(@RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String username,
                                             @RequestParam String email,
                                             @RequestParam String address){

        System.out.println();
        //查询总条数
        username = "%" + username + "%" ;
        email = "%" + email + "%" ;
        address = "%" + address + "%" ;
        Integer total = userMapper.selectTotal(username,email,address);
        //进行分页查询加上username的模糊查询
        pageNum = (pageNum - 1) * pageSize;
        List<User> data = userMapper.selectPage(pageNum, pageSize,username,email,address);
        //用map封装数据
        HashMap<String, Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);

        return res;
    }*/

    //mybatis-plus实现分页
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address) {
//        IPage<User> page = new Page<>(pageNum, pageSize);
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if (!"".equals(username)) {
//            queryWrapper.like("username", username);
//        }
//        if (!"".equals(nickname)) {
//            queryWrapper.like("nickname", nickname);
//        }
//        if (!"".equals(address)) {
//            queryWrapper.like("address", address);
//        }
//        queryWrapper.orderByDesc("id");
//        return userService.page(page, queryWrapper);
        return Result.success(userService.findPage(new Page<>(pageNum,pageSize),username,email,address));
    }


    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

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
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return true;
    }

    //登入接口
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }

    //注册接口
    @PostMapping("/register")
    public Result register(@RequestBody User user){

        return userService.register(user);
    }

    //修改接口
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }


    //个人信息修改接口
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        return Result.success(userService.getOne(userQueryWrapper));
    }
}
