package com.itxu.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.pojo.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itxu
 * @since 2022-03-31
 */
public interface CourseMapper extends BaseMapper<Course> {


    Page<Course> findPage(Page<Object> page,@Param("name") String name);


    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);


    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
}
