package com.kaige.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    @TableId
    private Long id;

    //用户名
    @NotNull(message = "用户名不能为空")

    private String userName;
    //昵称
    @NotNull(message = "昵称不能为空")

    private String nickName;
    @NotNull(message = "密码不能为空")

    //账号状态（0正常 1停用）
    private String status;
    @NotNull(message = "邮箱不能为空")

//邮箱
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //创建时间
    private LocalDateTime createTime;
    //更新人
    private Long updateBy;
    //更新时间
    private LocalDateTime updateTime;

}
