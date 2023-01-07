package com.example.project2.controller;

import com.example.project2.entity.Ticket;
import com.example.project2.repository.DepartmentRepo;
import com.example.project2.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    TicketRepo ticketRepo;

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("departmentList", departmentRepo.findAll());
        return "ticket/add.html";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute Ticket t) {
        ticketRepo.save(t);
        return "redirect:/ticket/search";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "departmentId", required = false) Integer departmentId,

                         // cac input params khác
                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {

        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);
        /// lam tuong tu giong department, viet cac ham trong repo va if else theo tham
        /// so params ơ tren

        //.....

        // show lai form
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        // show trong form search
        model.addAttribute("departmentList", departmentRepo.findAll());

        return "ticket/search.html";
    }

    @GetMapping("/get/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", ticketRepo.findById(id).orElse(null));
        return "ticket/detail.html";
    }

    @GetMapping("/delete") // ?id=1
    public String delete(@RequestParam("id") int id) {
        ticketRepo.deleteById(id);
        return "redirect:/ticket/search";
    }

    @GetMapping("/edit") // ?id=1
    public String edit(@RequestParam("id") int id, Model model) {
        model.addAttribute("ticket", ticketRepo.findById(id).orElse(null));
        return "ticket/edit.html";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Ticket editTicket) {
        Ticket current = ticketRepo.findById(editTicket.getTiketId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setClientName(editTicket.getClientName());
            // ... set them thuoc tinh khac
            ticketRepo.save(current);
        }

        return "redirect:/ticket/search";
    }
}