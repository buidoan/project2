package com.example.project2.controller;

import com.example.project2.entity.Department;
import com.example.project2.entity.User;
import com.example.project2.entity.UserRole;
import com.example.project2.repository.UserRepo;
import com.example.project2.repository.UserRoleRepo;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRoleRepo userRoleRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("user", new User());
        return "user/add.html";
    }

    @PostMapping("/upload-multi")
    public String add(@RequestParam("files") MultipartFile[] files) throws IllegalStateException, IOException {
        System.out.println(files.length);
        for (MultipartFile file : files)
            if (!file.isEmpty()) {
                final String UPLOAD_FOLDER = "D:/file_project2/";

                String filename = file.getOriginalFilename();
                File newFile = new File(UPLOAD_FOLDER + filename);

                file.transferTo(newFile);
            }

        return "redirect:/user/search";
    }

    @PostMapping("/new")
    public String add(
            @ModelAttribute("user") @Valid User u, BindingResult bindingResult,
            @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        if (bindingResult.hasErrors()) {
            return "user/add.html";
        }
        if (!file.isEmpty()) {
            final String UPLOAD_FOLDER = "D:/file_project2/";

            String filename = file.getOriginalFilename();
            File newFile = new File(UPLOAD_FOLDER + filename);

            file.transferTo(newFile);

            u.setAvatar(filename);// save to db
        }
        userRepo.save(u);
        UserRole userRole = new UserRole();
        userRole.setUser(u);
        userRole.setRole("member");
        userRoleRepo.save(userRole);
        return "redirect:/user/search";
    }

    /// /user/download?filename=abc.jpg
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        final String UPLOAD_FOLDER = "D:/file_project2/";

        File file = new File(UPLOAD_FOLDER + filename);

        Files.copy(file.toPath(), response.getOutputStream());
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
            List<User> users = userRepo.findAllById(Arrays.asList(id));

            model.addAttribute("totalPage", 1);
            model.addAttribute("count", users.size());
            model.addAttribute("userList", users);
        } else {
            Page<User> pageRS = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageRS = userRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageRS = userRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageRS = userRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageRS = userRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageRS = userRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageRS = userRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageRS = userRepo.searchByEndDate(end, pageable);
            } else {
                pageRS = userRepo.findAll(pageable);
            }

            model.addAttribute("totalPage", pageRS.getTotalPages());
            model.addAttribute("count", pageRS.getTotalElements());
            model.addAttribute("userList", pageRS.getContent());
        }

        //luu lai du lieu set sang view lai
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "user/search.html";
    }


    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
        userRepo.deleteById(id);
        return "redirect:/user/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("user", userRepo.findById(id).orElse(null));
        return "user/edit.html";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("user") @Valid User editUser,
            BindingResult bindingResult, @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "user/edit.html";
        }

        if (!file.isEmpty()) {
            final String UPLOAD_FOLDER = "D:/file_project2/";

            String filename = file.getOriginalFilename();
            File newFile = new File(UPLOAD_FOLDER + filename);

            file.transferTo(newFile);

            editUser.setAvatar(filename);// save to db
        }

        User current = userRepo.findById(editUser.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editUser.getName());
            current.setUsername(editUser.getUsername());
            current.setPassword(editUser.getPassword());
            current.setAvatar(editUser.getAvatar());
            userRepo.save(current);
        }

        return "redirect:/user/search";
    }
}
