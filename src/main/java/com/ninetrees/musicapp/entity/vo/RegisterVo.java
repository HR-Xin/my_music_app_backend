package com.ninetrees.musicapp.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Classname:RegisterVo
 * Description:
 */
@Data
public class RegisterVo {
    @ApiModelProperty(value = "用户名（唯一）")
    private String username;

    @ApiModelProperty(value = "存储加密后的密码")
    private String password;

    @ApiModelProperty(value = "用户邮箱（唯一）")
    private String email;

    @ApiModelProperty(value = "验证码")
    private String code;
}
