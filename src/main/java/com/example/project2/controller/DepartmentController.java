package com.example.project2.controller;

import com.example.project2.entity.Department;
import com.example.project2.repository.DepartmentRepo;
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
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentRepo departmentRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("deparment", new Department());
        return "department/add.html";
    }

    @PostMapping("/new")
    public String add(
            @ModelAttribute("department") @Valid Department department,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "department/add.html";
        }
        departmentRepo.save(department);
        return "redirect:/department/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,

                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Department> departments = departmentRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", departments.size());
            model.addAttribute("departmentList", departments);
        } else {
            Page<Department> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = departmentRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = departmentRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = departmentRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = departmentRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = departmentRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = departmentRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = departmentRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = departmentRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("departmentList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "department/search.html";
    }

    @GetMapping("/get/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("department", departmentRepo.findById(id).orElse(null));
        return "department/detail.html";
    }

    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
        departmentRepo.deleteById(id);
        return "redirect:/department/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("department", departmentRepo.findById(id).orElse(null));
        return "department/edit.html";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("department") @Valid Department editDeparment,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "department/edit.html";
        }

        Department current = departmentRepo.findById(editDeparment.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editDeparment.getName());
            departmentRepo.save(current);
        }

        return "redirect:/department/search";
    }
}
