package com.chachae.exam.rest;

import com.chachae.exam.common.model.Type;
import com.chachae.exam.core.annotation.Permissions;
import com.chachae.exam.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chachae
 * @since 2020/2/29 22:12
 */
@RestController
@RequestMapping("/api/type")
public class TypeController {

  @Resource private TypeService typeService;

  @GetMapping
  @Permissions("type:list")
  public List<Type> list() {
    return this.typeService.list();
  }
}
