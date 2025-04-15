package com.ninetrees.musicapp.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author hr_xin
 * @since 2025-03-21
 */
@Data
//当callSuper = false时（这是默认值），生成的equals和hashCode方法
// 不会调用父类的equals和hashCode方法，而是只基于当前类的字段来生成这些方法。
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Users对象", description = "")
@ToString
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "用户名（唯一）")
    private String username;

    @ApiModelProperty(value = "存储加密后的密码")
    private String password;

    @ApiModelProperty(value = "用户邮箱（唯一）")
    private String email;

    private String avatar;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "个人签名")
    private String bio;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @ApiModelProperty(value = "资料更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;


}
