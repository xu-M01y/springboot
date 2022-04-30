package com.itxu.springboot.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itxu.springboot.pojo.dto.UserPasswordDTO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itxu.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author show
 * @create 2022-03-22-16:06
 */

@Repository

public interface UserMapper extends BaseMapper<User>{

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> page,@Param("username") String username,@Param("email") String email,@Param("address") String address);

    /*Integer deleteByName(String username);*/
/*

    @Select("SELECT * FROM sys_user where username like #{username} and email like #{email} and address like #{address} LIMIT #{pageNum},#{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize,String username,String email,String address);
    @Select("SELECT count(*) FROM sys_user where username like #{username} and email like #{email} and address like #{address}")
    Integer selectTotal(String username,String email,String address);
*/




}
