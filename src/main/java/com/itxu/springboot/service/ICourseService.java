package com.itxu.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itxu
 * @since 2022-03-31
 */
public interface ICourseService extends IService<Course> {


    Page<Course> findPage(Page<Object> page, String name);

    void setStudentCourse(Integer courseId, Integer studentId);

    void deleteStudentCourse(Integer courseId, Integer studentId);
}
