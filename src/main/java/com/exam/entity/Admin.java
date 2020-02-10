package com.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员实体类
 *
 * @author yzn
 * @date 2020/1/15
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin extends Model<Admin> {

  @TableId(type = IdType.AUTO)
  private Integer id;
  // 姓名
  private String name;
  // 工号
  private String number;
  // 密码
  private String password;
  // 角色id
  private Integer roleId;
  // 最后登录时间
  private Date lastLoginTime;

  /**
   * 获取主键值
   *
   * @return 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
