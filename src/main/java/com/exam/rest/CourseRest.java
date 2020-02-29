package com.exam.rest;

import com.exam.common.R;
import com.exam.entity.Course;
import com.exam.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chachae
 * @since 2020/2/27 16:25
 */
@RestController
@RequestMapping("/api/course")
public class CourseRest {

  @Resource private CourseService courseService;

  @GetMapping("/teacher/{teacherId}")
  public List<Course> listByTeacherId(@PathVariable Integer teacherId) {
    return this.courseService.listByTeacherId(teacherId);
  }

  /**
   * 添加课程
   *
   * @param courseName 课程名称
   * @param teacherId 教师ID
   * @return 成功信息
   */
  @GetMapping("/save")
  public R newCourse(String courseName, Integer teacherId) {
    // 封装参数
    Course build = Course.builder().courseName(courseName).teacherId(teacherId).build();
    this.courseService.save(build);
    return R.success();
  }

  /**
   * 删除课程
   *
   * @param id 课程ID
   * @return 删除成功信息
   */
  @GetMapping("/delete{id}")
  public R delCourse(@PathVariable Integer id) {
    // 调用课程删除接口
    this.courseService.removeById(id);
    return R.success();
  }
}
