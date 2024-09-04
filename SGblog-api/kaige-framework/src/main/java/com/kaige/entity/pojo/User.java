package com.kaige.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2024-08-12 18:14:49
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User {
    @TableId
    private Long id;

//用户名
@NotBlank(message = "用户名不能为空")

    private String userName;
//昵称
@NotBlank(message = "昵称不能为空")
    private String nickName;
    @NotNull(message = "密码不能为空")

//密码
    private String password;
//用户类型：0代表普通用户，1代表管理员
    private String type;
//账号状态（0正常 1停用）
    private String status;
    @NotBlank(message = "邮箱不能为空")

//邮箱
    private String email;
//手机号
    private String phonenumber;
//用户性别（0男，1女，2未知）
    private String sex;
//头像
    private String avatar;
//创建人的用户id
    private Long createBy;
//创建时间
    private LocalDateTime createTime;
//更新人
    private Long updateBy;
//更新时间
    private LocalDateTime updateTime;
//删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //关联角色id数组，非user表字段
    @TableField(exist = false)
    private Long[] roleIds;
}

