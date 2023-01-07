package com.example.project2.controller;

import com.example.project2.entity.Course;
import com.example.project2.entity.Student;
import com.example.project2.repository.CourseRepo;
import com.example.project2.repository.StudentRepo;
import com.example.project2.repository.UserRepo;
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
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    StudentRepo studentRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("course", new Course());
//        model.addAttribute("studentList", studentRepo.findAll());
        return "course/add.html";
    }

    @PostMapping("/new")
    public String add(
            @ModelAttribute("course") @Valid Course course,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "course/add.html";
        }
        courseRepo.save(course);
        return "redirect:/course/search";
    }


    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "courseName", required = false) String courseName,

                         @RequestParam(name = "studentCode", required = false) String studentCode,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Course> courseList = courseRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", courseList.size());
            model.addAttribute("courseList", courseList);
        } else {
            Page<Course> pageRS = null;

            if (StringUtils.hasText(studentCode) && StringUtils.hasText(courseName)) {
                pageRS = courseRepo.searchByCourseNameAndStudentCode("%" +courseName  + "%", "%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(studentCode)) {
                pageRS = courseRepo.searchByStudentCode("%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(courseName)) {
                pageRS = courseRepo.searchByCourseName("%" + courseName + "%", pageable);
            } else {
                pageRS = courseRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("courseList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("courseName",courseName);


        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "course/search.html";
    }


    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
     courseRepo.deleteById(id);
        return "redirect:/course/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("course", courseRepo.findById(id).orElse(null));

        return "course/edit.html";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("course") @Valid Course editCourse,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "student/edit.html";
        }
       Course current = courseRepo.findById(editCourse.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
          current.setName(editCourse.getName());
            courseRepo.save(current);
        }

        return "redirect:/course/search";
    }
}
