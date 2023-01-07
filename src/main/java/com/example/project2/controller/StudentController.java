package com.example.project2.controller;

import com.example.project2.entity.Department;
import com.example.project2.entity.Student;
import com.example.project2.repository.StudentRepo;
import com.example.project2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("userList", userRepo.findAll());
        return "student/add.html";
    }

    @PostMapping("/new")
    public String add(
            @ModelAttribute("student") @Valid Student student,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "student/add.html";
        }
        studentRepo.save(student);
        return "redirect:/student/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "studentCode", required = false) String studentCode,

                         @RequestParam(name = "userName", required = false) String userName,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Student> students = studentRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", students.size());
            model.addAttribute("studentList", students);
        } else {
            Page<Student> pageRS = null;

            if (StringUtils.hasText(studentCode) && StringUtils.hasText(userName)) {
                pageRS = studentRepo.searchByStudentCodeAndUserName("%" + studentCode + "%", "%" + userName + "%", pageable);
            } else if (StringUtils.hasText(studentCode)) {
                pageRS = studentRepo.searchByStudentCode("%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(userName)) {
                pageRS = studentRepo.searchByUserName("%" + userName + "%", pageable);
            } else {
                pageRS = studentRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("studentList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("studentCode", studentCode);
        model.addAttribute("userName", userName);


        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "student/search.html";
    }

    @GetMapping("/get/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentRepo.findById(id).orElse(null));
        return "student/detail.html";
    }

    //
    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
        studentRepo.deleteById(id);
        return "redirect:/student/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("student", studentRepo.findById(id).orElse(null));
        model.addAttribute("userList", userRepo.findAll());
        return "student/edit.html";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("student") @Valid Student editStudent,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "student/edit.html";
        }
        Student current = studentRepo.findById(editStudent.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setStudentCode(editStudent.getStudentCode());
            current.setUser(editStudent.getUser());
            studentRepo.save(current);
        }

        return "redirect:/student/search";
    }
}
