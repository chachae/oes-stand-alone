package com.exam.rest;

import com.exam.entity.Type;
import com.exam.service.TypeService;
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
public class TypeRest {

  @Resource private TypeService typeService;

  @GetMapping
  public List<Type> list() {
    return this.typeService.list();
  }
}
