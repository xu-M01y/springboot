package com.itxu.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.pojo.Course;
import com.itxu.springboot.mapper.CourseMapper;
import com.itxu.springboot.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itxu
 * @since 2022-03-31
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<Course> findPage(Page<Object> page, String name) {
        return courseMapper.findPage(page, name);
    }

    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.setStudentCourse(courseId, studentId);
    }


    @Override
    public void deleteStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
    }
}
