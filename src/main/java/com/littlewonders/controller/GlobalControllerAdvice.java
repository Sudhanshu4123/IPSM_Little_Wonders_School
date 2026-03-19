package com.littlewonders.controller;

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

    @ModelAttribute
    public void addVisitorCount(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        jakarta.servlet.http.HttpSession session = request.getSession();
        
        // Increment count only for NEW sessions and page views (not static assets/admin)
        boolean isPageView = !uri.contains(".") && !uri.startsWith("/api") && !uri.startsWith("/admin");
        
        if (isPageView) {
            if (session.getAttribute("visitor_counted") == null) {
                Long count = visitorCountService.incrementAndGet();
                session.setAttribute("visitor_counted", true);
                model.addAttribute("visitorCount", count);
            } else {
                model.addAttribute("visitorCount", visitorCountService.getCount());
            }
        } else {
            model.addAttribute("visitorCount", visitorCountService.getCount());
        }
    }
}
