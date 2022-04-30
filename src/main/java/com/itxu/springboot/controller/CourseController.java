package com.itxu.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.common.Result;
import com.itxu.springboot.pojo.User;
import com.itxu.springboot.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.itxu.springboot.service.ICourseService;
import com.itxu.springboot.pojo.Course;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author itxu
 * @since 2022-03-31
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @Resource
    private UserService userService;

    @Transactional
    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public Result studentCourse(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        courseService.deleteStudentCourse(courseId, studentId);
        courseService.setStudentCourse(courseId, studentId);
        return Result.success();
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        courseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        courseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(courseService.list());
    }

    @GetMapping("/{id}")
    public Course findOne(@PathVariable Integer id) {
        return courseService.getById(id);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        //逻辑关联
//        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        Page<Course> coursePage = courseService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Course> records = coursePage.getRecords();
//        for (Course record : records) {
//                User user = userService.getById(record.getTeacherId());
//                if (null != user){
//                        record.setTeacher(user.getNickname());
//                }
//        }


        //--------多表关联查询
        Page<Course> coursePage = courseService.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(coursePage);
    }

}

