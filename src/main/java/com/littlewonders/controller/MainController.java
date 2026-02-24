package com.littlewonders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/admission")
    public String admission() {
        return "admission";
    }

    @GetMapping("/gallery")
    public String gallery() {
        return "gallery";
    }

    @GetMapping("/toddler")
    public String preschool() {
        return "toddler";
    }

    @GetMapping("/pre-nursery")
    public String nursery() {
        return "prenursery";
    }

    @GetMapping("/nursery")
    public String lkg() {
        return "nursery";
    }

    @GetMapping("/prep")
    public String ukg() {
        return "prep";
    }

    @GetMapping("/methodology")
    public String methodology() {
        return "methodology";
    }

    @GetMapping("/facilities")
    public String facilities() {
        return "facilities";
    }

    @GetMapping("/speciality")
    public String speciality() {
        return "speciality";
    }

    @GetMapping("/admission-form")
    public String admissionForm() {
        return "admission-form";
    }

    @GetMapping("/admission-faq")
    public String admissionFaq() {
        return "admission-faq";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/holidays")
    public String holidays() {
        return "holidays";
    }

    @GetMapping("/celebrations")
    public String celebrations() {
        return "celebrations";
    }

    @GetMapping("/mission")
    public String mission() {
        return "mission";
    }

    @GetMapping("/vision")
    public String vision() {
        return "mission";
    }

    @GetMapping("/branches")
    public String branches() {
        return "branches";
    }

    @GetMapping("/careers")
    public String careers() {
        return "careers";
    }

    @GetMapping("/franchise")
    public String franchise() {
        return "franchise";
    }
}
