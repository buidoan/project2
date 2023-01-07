package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LanguageController {

    @GetMapping("/change-lang")
    public String changeLang() {
        // su dung cau hinh o application , chi tao url de mang tinh chat mapping
        return "redirect:/department/search";// chuyen huong ve 1 trang mac dinh
    }
}
