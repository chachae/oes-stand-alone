package com.exam.rest;

import cn.hutool.core.util.StrUtil;
import com.exam.common.R;
import com.exam.constant.SysConsts;
import com.exam.entity.Paper;
import com.exam.entity.Question;
import com.exam.entity.StuAnswerRecord;
import com.exam.entity.dto.AnswerEditDto;
import com.exam.entity.dto.ImportPaperDto;
import com.exam.entity.dto.ImportPaperRandomQuestionDto;
import com.exam.entity.dto.PaperQuestionUpdateDto;
import com.exam.service.PaperService;
import com.exam.service.QuestionService;
import com.exam.service.ScoreService;
import com.exam.service.StuAnswerRecordService;
import com.exam.util.HttpContextUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author chachae
 * @since 2020/2/29 12:11
 */
@RestController
@RequestMapping("/api/paper")
public class PaperRest {

  @Resource private ScoreService scoreService;
  @Resource private StuAnswerRecordService stuAnswerRecordService;
  @Resource private QuestionService questionService;
  @Resource private PaperService paperService;

  @PostMapping("/update")
  public R updateTime(Paper paper) {
    this.paperService.updateById(paper);
    return R.success();
  }

  @GetMapping("/{id}")
  public Paper getOne(@PathVariable Integer id) {
    return this.paperService.getById(id);
  }

  @ResponseBody
  @PostMapping("/import/excel")
  public R excel(@RequestParam("file") MultipartFile multipartFile) {
    // 导入试卷
    ImportPaperDto dto = this.questionService.importPaper(multipartFile);
    return R.successWithData(dto);
  }

  @PostMapping("/save/import")
  public R newPaperByExcel(Paper paper, ImportPaperRandomQuestionDto entity) {
    // 获取教师 ID
    Integer teacherId = (Integer) HttpContextUtil.getAttribute(SysConsts.SESSION.TEACHER_ID);
    // 设置出卷老师
    paper.setTeacherId(teacherId);
    // 局部随机参数判断，没有局部随机参数则调用普通的插入接口
    boolean a = entity.getA() == 0,
        b = entity.getB() == 0,
        c = entity.getC() == 0,
        d = entity.getD() == 0,
        e = entity.getE() == 0,
        f = entity.getF() == 0;
    if (a && b && c && d && e && f) {
      this.paperService.save(paper);
    } else {
      this.paperService.saveWithImportPaper(paper, entity);
    }
    return R.successWithData(paper.getId());
  }

  /**
   * 提交组卷信息
   *
   * @param paper 试卷信息
   * @param paperFormId 试卷模板ID
   * @return 组卷页面
   */
  @PostMapping("/save/random")
  public R add(Paper paper, Integer paperFormId, String difficulty) {
    // 设置试卷模板 ID
    paper.setPaperFormId(paperFormId);
    // 获取教师 ID
    Integer teacherId = (Integer) HttpContextUtil.getAttribute(SysConsts.SESSION.TEACHER_ID);
    // 调用组卷接口
    paper.setTeacherId(teacherId);
    // 判断是否指定难度
    if (StrUtil.isBlank(difficulty)) {
      paperService.randomNewPaper(paper);
    } else {
      // 带指定难度的接口
      paperService.randomNewPaper(paper, difficulty);
    }
    return R.successWithData(paper.getId());
  }

  /**
   * 修改主观题成绩
   *
   * @param dto 信息
   */
  @ResponseBody
  @PostMapping("/update/score")
  public R editScore(AnswerEditDto dto) {
    // 修改该题得分
    StuAnswerRecord record = new StuAnswerRecord();
    record.setId(dto.getId()).setScore(dto.getNewScore());
    this.stuAnswerRecordService.updateById(record);
    // 修改总分
    StuAnswerRecord stuRec = this.stuAnswerRecordService.getById(dto.getId());
    // 封装参数
    dto.setStuId(stuRec.getStuId());
    dto.setPaperId(stuRec.getPaperId());
    this.scoreService.updateScoreByStuIdAndPaperId(dto);
    return R.success();
  }

  /**
   * 级联删除试卷、分数、答案记录
   *
   * @param id 试卷ID
   * @return 试卷页面
   */
  @ResponseBody
  @PostMapping("/delete/{id}")
  public R delPaper(@PathVariable Integer id) {
    // 级联删除试卷（详见接口实现类）
    paperService.deletePaperById(id);
    return R.success();
  }

  /**
   * 修改試卷题目答案
   *
   * @param question 问题信息
   * @return 回调信息
   */
  @ResponseBody
  @PostMapping("/update/answer")
  public R updateAnswer(Question question) {
    this.questionService.updateById(question);
    return R.success();
  }

  /**
   * 修改試卷题目
   *
   * @param dto 修改的信息
   * @return 回调信息
   */
  @ResponseBody
  @PostMapping("/update/question")
  public R updateQuestionId(PaperQuestionUpdateDto dto) {
    this.paperService.updateQuestionId(dto);
    return R.success();
  }
}
