package com.example.project2.controller;

import com.example.project2.entity.Course;
import com.example.project2.entity.Score;
import com.example.project2.repository.CourseRepo;
import com.example.project2.repository.ScoreRepo;
import com.example.project2.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoreRepo scoreRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    StudentRepo studentRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("score", new Score());
        model.addAttribute("studentList", studentRepo.findAll());
        model.addAttribute("courseList", courseRepo.findAll());
        return "score/add.html";
    }

    @PostMapping("/new")
    public String add(
            @ModelAttribute("score") @Valid Score score,
            BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentList", studentRepo.findAll());
            model.addAttribute("courseList", courseRepo.findAll());
            return "score/add.html";
        }
        scoreRepo.save(score);
        return "redirect:/score/search";
    }


    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "courseName", required = false) String courseName,
                         @RequestParam(name = "score", required = false ,defaultValue = "0") double score,
                         @RequestParam(name = "studentCode", required = false) String studentCode,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Score> scoreList = scoreRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", scoreList.size());
            model.addAttribute("scoreList", scoreList);
        } else {
            Page<Score> pageRS = null;

            if (StringUtils.hasText(studentCode) && StringUtils.hasText(courseName) && score != 0) {
                pageRS = scoreRepo.searchByScoreAndCourseNameAndStudentCode("%" + courseName + "%", "%" + studentCode + "%", score, pageable);
            } else if (StringUtils.hasText(studentCode) && StringUtils.hasText(courseName)) {
                pageRS = scoreRepo.searchByStudentCodeAndCourseName("%" + studentCode + "%", courseName, pageable);
            } else if (StringUtils.hasText(courseName) && score != 0) {
                pageRS = scoreRepo.searchByCourseNameAndScore("%" + courseName + "%", score, pageable);
            } else if (StringUtils.hasText(studentCode) && score != 0) {
                pageRS = scoreRepo.searchByStudentCodeAndScore("%" + courseName + "%", score, pageable);
            }else if (StringUtils.hasText(studentCode)) {
                pageRS = scoreRepo.searchByStudentCode("%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(courseName)) {
                pageRS = scoreRepo.searchByCourseName("%" + courseName + "%", pageable);
            } else if(score!=0){
                pageRS = scoreRepo.searchByScore( score , pageable);
            } else {
                pageRS = scoreRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("scoreList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("courseName", courseName);
        model.addAttribute("score", score);


        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "score/search.html";
    }


    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
     scoreRepo.deleteById(id);
        return "redirect:/score/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("score", scoreRepo.findById(id).orElse(null));

        return "score/edit.html";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("score") @Valid Score editScore,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "score/edit.html";
        }
       Score current = scoreRepo.findById(editScore.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
       current.setScore(editScore.getScore());
       current.setCourse(editScore.getCourse());
       current.setStudent(editScore.getStudent());
       scoreRepo.save(current);

        }

        return "redirect:/score/search";
    }
}
