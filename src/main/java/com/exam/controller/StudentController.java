package com.exam.controller;

import cn.hutool.core.util.ObjectUtil;
import com.exam.constant.SysConsts;
import com.exam.entity.*;
import com.exam.entity.dto.ChangePassDto;
import com.exam.exception.ServiceException;
import com.exam.service.*;
import com.exam.util.HttpContextUtil;
import com.exam.util.NumberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * 學生控制层
 *
 * @author yzn
 * @date 2020/2/10
 */
@Controller
public class StudentController {

  @Resource private StudentService studentService;
  @Resource private TeacherService teacherService;
  @Resource private PaperService paperService;
  @Resource private ScoreService scoreService;
  @Resource private QuestionService questionService;
  @Resource private MajorService majorService;

  /**
   * 转发到登录界面
   *
   * @return 学生登录界面
   */
  @GetMapping("/")
  public String login() {
    return "login";
  }

  /**
   * 学生登录 验证学号和密码
   *
   * @param studentId 学号(stuNumber)
   * @param studentPassword 密码
   * @param r r 重定向对象
   * @return 主界面
   */
  @PostMapping("/")
  public String login(String studentId, String studentPassword, RedirectAttributes r) {
    // 获取 session 对象
    HttpSession session = HttpContextUtil.getSession();
    try {
      // 执行登录接口
      Student student = studentService.login(studentId, studentPassword);
      // 设置 Session 信息
      session.setAttribute(SysConsts.SESSION.STUDENT_ID, student.getId());
      session.setAttribute(SysConsts.SESSION.STUDENT, student);
      // 重定向到学生主界面
      return "redirect:/student/home/" + student.getId();
    } catch (ServiceException e) {
      // 捕获登录异常返回登录界面并提示异常信息
      r.addFlashAttribute("message", e.getMessage());
      return "redirect:/";
    }
  }

  /**
   * 学生登录后来到home页
   *
   * @param id 学生ID
   * @param model model 对象
   * @return 学生主界面
   */
  @GetMapping("/student/home/{id}")
  public String home(@PathVariable Integer id, Model model) {
    // 调用通过ID查询接口
    Student student = studentService.findById(id);
    model.addAttribute("student", student);
    return "student/home";
  }

  /**
   * 学生修改密码
   *
   * @param id 学生ID
   * @return 密码修改界面
   */
  @GetMapping("/student/{id}/changePass")
  public String changePass(@PathVariable Integer id) {
    return "student/changePass";
  }

  /**
   * 密码修改
   *
   * @param id 学生ID
   * @param dto 密码信息
   * @return 重定向到登录界面
   */
  @PostMapping("/student/{id}/changePass")
  public String changePass(@PathVariable Integer id, ChangePassDto dto, RedirectAttributes r) {
    // 通过获取 Session 对象
    HttpSession session = HttpContextUtil.getSession();
    try {
      // 调用密码修改接口
      studentService.changePassword(id, dto);
    } catch (ServiceException e) {
      r.addFlashAttribute("message", e.getMessage());
      return "redirect:/student/" + id + "/changePass";
    }
    // 移除学生 session 信息
    session.removeAttribute(SysConsts.SESSION.STUDENT_ID);
    session.removeAttribute(SysConsts.SESSION.STUDENT);
    return "redirect:/";
  }

  /**
   * 学生退出
   *
   * @return 登录界面
   */
  @GetMapping("/logout")
  public String logout() {
    // 通过 SpringContext 上下文获取 Session 对象
    HttpSession session = HttpContextUtil.getSession();
    // 移除学生 session 信息
    session.removeAttribute(SysConsts.SESSION.STUDENT_ID);
    session.removeAttribute(SysConsts.SESSION.STUDENT);
    return "redirect:/";
  }

  /**
   * 查看系统公告列表
   *
   * @param model model 对象
   * @return 公告集合
   */
  @GetMapping("/student/announce/system")
  public String announceSystem(Model model) {
    // 嗲用查询全部公告的借口
    List<Announce> announceList = teacherService.findAllSystemAnnounce();
    // 设置 model 对象信息
    model.addAttribute("announceList", announceList);
    return "student/sysAnnounceList";
  }

  /**
   * 学生进入我的考试列表
   *
   * @param model model 对象
   * @return 学生考试信息页面
   */
  @GetMapping("/student/exam")
  public String exam(Model model) {
    // 通过 SpringContext 上下文获取 Session 对象
    HttpSession session = HttpContextUtil.getSession();
    // 获取学生的ID
    Integer id = (Integer) session.getAttribute(SysConsts.SESSION.STUDENT_ID);
    Student student = studentService.findById(id);
    // 查询所有正式试卷
    List<Paper> paperList = paperService.findPaperByMajorId(student.getMajorId());
    model.addAttribute("paperList", paperList);
    // 模拟试卷
    List<Paper> practicePaperList = paperService.findPracticePapersByMajorId(student.getMajorId());
    model.addAttribute("practicePaperList", practicePaperList);
    return "student/examList";
  }

  /**
   * 学生进入考试
   *
   * @param stuId 学生ID
   * @param paperId 试卷ID
   * @param model model 对象
   * @return 考试页面
   */
  @GetMapping("/student/{stuId}/paper/{paperId}")
  public String doPaper(@PathVariable Integer stuId, @PathVariable Integer paperId, Model model) {
    // 通过 ID 查询试卷信息并将各种题型的题目放进 Set 集合中返回给前端，显示成试卷
    Paper paper = paperService.getById(paperId);
    Set<Question> qChoiceList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.CHOICE_TYPE);
    Set<Question> qMulChoiceList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.MUL_CHOICE_TYPE);
    Set<Question> qTofList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.TOF_TYPE);
    Set<Question> qFillList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.FILL_TYPE);
    Set<Question> qSaqList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.SAQ_TYPE);
    Set<Question> qProgramList =
        paperService.findQuestionsByPaperIdAndType(paperId, SysConsts.QUESTION.PROGRAM_TYPE);
    // 设置 model 对象信息
    model.addAttribute("paper", paper);
    model.addAttribute("qChoiceList", qChoiceList);
    model.addAttribute("qMulChoiceList", qMulChoiceList);
    model.addAttribute("qTofList", qTofList);
    model.addAttribute("qFillList", qFillList);
    model.addAttribute("qSaqList", qSaqList);
    model.addAttribute("qProgramList", qProgramList);
    // 返回试卷
    return "student/paperDetail";
  }

  /**
   * 学生进行考试 批改试卷
   *
   * @param stuId 学生ID
   * @param paperId 试卷ID
   * @return 学生主页
   */
  @PostMapping("/student/{stuId}/paper/{paperId}")
  public String doPaper(@PathVariable Integer stuId, @PathVariable Integer paperId) {
    // 获取 request 对象
    HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
    // 批改试卷
    paperService.markPaper(stuId, paperId, request);
    return "redirect:/student/home/" + stuId;
  }

  /**
   * 查询成绩列表
   *
   * @param id 学生ID
   * @param model model 对象
   * @return 成绩列表
   */
  @GetMapping("/student/score/{id}")
  public String scoreList(@PathVariable Integer id, Model model) {
    // 通过学生 ID 查询成绩集合
    List<Score> scoreList = scoreService.findByStuId(id);
    // 设置 model 对象
    model.addAttribute("scoreList", scoreList);
    return "student/scoreList";
  }

  /**
   * 学生帮助中心
   *
   * @return 学生帮助中心页面
   */
  @GetMapping("/student/help")
  public String help() {
    return "student/help";
  }

  /**
   * 查询问题
   *
   * @param questionId 问题ID
   * @param model model 对象
   * @return 问题信息
   */
  @GetMapping("/search")
  public String searchQuestion(String questionId, Model model) {
    // 判断是否为数字
    if (!NumberUtil.isNumber(questionId)) {
      String message = "温馨提示：搜索框只能输入纯数字！";
      model.addAttribute("message", message);
      return "student/home";
    } else {
      // 通过ID查询问题信息
      Question question = questionService.findById(Integer.parseInt(questionId));
      if (ObjectUtil.isNotEmpty(question)) {
        // 通过课程 ID 查询课程信息
        Course course = questionService.findByCourseId(question.getCourseId());
        model.addAttribute("question", question);
        model.addAttribute("course", course);
        return "student/question";
      } else {
        String message = "题号不存在，请勿乱输！";
        model.addAttribute("message", message);
        return "student/home";
      }
    }
  }
}