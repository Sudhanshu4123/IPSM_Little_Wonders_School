package com.littlewonders.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.littlewonders.repository.BlogRepository;
import com.littlewonders.repository.GalleryRepository;
import com.littlewonders.repository.CelebrationRepository;
import com.littlewonders.repository.BranchRepository;

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

    @Autowired
    private GalleryRepository galleryRepository;

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("galleryItems", galleryRepository.findByActiveTrueOrderByUploadDateDesc());
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

    @GetMapping("/admission/form/download")
    public String downloadBlankForm() {
        return "blank-admission-form";
    }

    @GetMapping("/admission-faq")
    public String admissionFaq() {
        return "admission-faq";
    }

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("blogs", blogRepository.findByActiveTrueOrderByPublishedDateDesc());
        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        blogRepository.findById(id).ifPresent(blog -> model.addAttribute("blog", blog));
        return "blog-detail";
    }

    @GetMapping("/holidays")
    public String holidays() {
        return "holidays";
    }

    @Autowired
    private CelebrationRepository celebrationRepository;

    @GetMapping("/celebrations")
    public String celebrations(Model model) {
        model.addAttribute("celebrations", celebrationRepository.findByActiveTrueOrderByEventDateDesc());
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

    @Autowired
    private BranchRepository branchRepository;

    @GetMapping("/branches")
    public String branches(Model model) {
        model.addAttribute("branches", branchRepository.findByActiveTrue());
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
