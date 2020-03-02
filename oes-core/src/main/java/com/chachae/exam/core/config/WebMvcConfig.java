package com.chachae.exam.core.config;

/**
 * @author chachae
 * @since 2020/3/1 23:55
 */
import com.chachae.exam.core.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * WebMvc 配置
 *
 * @author yzn
 * @date 2020/1/31
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  /** 基本拦截路径 */
  private static final String BASE_URI = "/**";

  /** 登录拦截器注入 */
  @Resource private LoginInterceptor loginInterceptor;

  /** 拦截器注册器 */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册登录拦截器
    registry.addInterceptor(loginInterceptor).addPathPatterns(BASE_URI);
  }
}