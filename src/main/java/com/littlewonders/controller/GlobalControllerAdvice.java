package com.littlewonders.controller;

import com.littlewonders.config.FeeConfig;
import com.littlewonders.repository.CourseRepository;
import com.littlewonders.service.VisitorCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private VisitorCountService visitorCountService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FeeConfig feeConfig;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        try {
            if (courseRepository != null) {
                model.addAttribute("courses", courseRepository.findAll());
            }
            if (feeConfig != null) {
                model.addAttribute("feeConfig", feeConfig);
            }
        } catch (Exception e) {
            System.err.println("Global attributes error: " + e.getMessage());
            // Provide empty defaults to prevent render crashes
            model.addAttribute("courses", java.util.Collections.emptyList());
        }
    }

    @ModelAttribute
    public void addVisitorCount(HttpServletRequest request, Model model) {
        try {
            String uri = request.getRequestURI();
            
            // Skip for static assets to avoid redundant processing
            if (uri.contains(".") || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images") || uri.startsWith("/api")) {
                return;
            }

            jakarta.servlet.http.HttpSession session = request.getSession(true);
            boolean isNewVisitor = session.getAttribute("visitor_counted") == null;

            if (isNewVisitor && !uri.startsWith("/admin")) {
                try {
                    Long count = visitorCountService.incrementAndGet();
                    session.setAttribute("visitor_counted", true);
                    model.addAttribute("visitorCount", count);
                } catch (Exception e) {
                    System.err.println("Visitor count increment failed: " + e.getMessage());
                    model.addAttribute("visitorCount", visitorCountService.getCount());
                }
            } else {
                model.addAttribute("visitorCount", visitorCountService.getCount());
            }
        } catch (Exception e) {
            System.err.println("GlobalControllerAdvice Visitor Error: " + e.getMessage());
            model.addAttribute("visitorCount", 100000L);
        }
    }
}
