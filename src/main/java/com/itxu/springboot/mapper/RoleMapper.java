package com.itxu.springboot.mapper;

import com.itxu.springboot.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.itxu.springboot.pojo.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where value = #{role}")
    Integer selectByValue(@Param("role") String role);
}




