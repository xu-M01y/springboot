package com.itxu.springboot.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author itxu
 * @date 2022/3/2919:02
 */
@Data
@TableName("sys_role_menu")
public class RoleMenu {

    private Integer roleId;

    private Integer menuId;
}
