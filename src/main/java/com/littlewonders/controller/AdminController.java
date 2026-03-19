package com.littlewonders.controller;

import com.littlewonders.model.*;
import com.littlewonders.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";

    @Autowired private BlogRepository blogRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AdmissionRepository admissionRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CelebrationRepository celebrationRepository;
    @Autowired private GalleryRepository galleryRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private SecurityAuditRepository securityAuditRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", userRepository.findAllByRoleName("ROLE_USER").size());
        model.addAttribute("totalBlogs", blogRepository.count());
        model.addAttribute("totalCourses", courseRepository.count());
        model.addAttribute("totalCelebrations", celebrationRepository.count());
        model.addAttribute("totalBranches", branchRepository.count());
        model.addAttribute("totalGalleryItems", galleryRepository.count());
        model.addAttribute("securityLogs", securityAuditRepository.findTop10ByOrderByAttemptedAtDesc());
        return "admin/dashboard";
    }

    // Branch management
    @GetMapping("/branches")
    public String manageBranches(Model model) {
        model.addAttribute("branches", branchRepository.findAll());
        return "admin/manage-branches";
    }

    @PostMapping("/branches/add")
    public String addBranch(@ModelAttribute Branch branch, @RequestParam("imageFile") MultipartFile imageFile) {
        String imagePath = saveImage(imageFile);
        if (imagePath != null) {
            branch.setImagePath(imagePath);
        }
        branchRepository.save(branch);
        return "redirect:/admin/branches";
    }

    @PostMapping("/branches/delete/{id}")
    public String deleteBranch(@PathVariable Long id) {
        branchRepository.deleteById(id);
        return "redirect:/admin/branches";
    }

    // Gallery management
    @GetMapping("/gallery")
    public String manageGallery(Model model) {
        model.addAttribute("galleryItems", galleryRepository.findAll());
        return "admin/manage-gallery";
    }

    @PostMapping("/gallery/add")
    public String addGalleryItem(@ModelAttribute GalleryItem galleryItem, 
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                // Ensure directory exists
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate unique filename
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Save file
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set image URL for static access
                galleryItem.setImageUrl("/images/uploads/" + fileName);
                
            } catch (IOException e) {
                e.printStackTrace();
                // Fallback or handle error (for now just use placeholder or skip)
            }
        }
        galleryRepository.save(galleryItem);
        return "redirect:/admin/gallery";
    }

    @PostMapping("/gallery/delete/{id}")
    public String deleteGalleryItem(@PathVariable Long id) {
        galleryRepository.deleteById(id);
        return "redirect:/admin/gallery";
    }

    // Celebration management
    @GetMapping("/celebrations")
    public String manageCelebrations(Model model) {
        model.addAttribute("celebrations", celebrationRepository.findAll());
        return "admin/manage-celebrations";
    }

    @PostMapping("/celebrations/add")
    public String addCelebration(@ModelAttribute Celebration celebration, @RequestParam("imageFile") MultipartFile imageFile) {
        String imagePath = saveImage(imageFile);
        if (imagePath != null) {
            celebration.setImagePath(imagePath);
        }
        celebration.setPublishedDate(LocalDateTime.now());
        celebrationRepository.save(celebration);
        return "redirect:/admin/celebrations";
    }

    @PostMapping("/celebrations/delete/{id}")
    public String deleteCelebration(@PathVariable Long id) {
        celebrationRepository.deleteById(id);
        return "redirect:/admin/celebrations";
    }

    // Blog management
    @GetMapping("/blogs")
    public String manageBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "admin/manage-blogs";
    }

    @PostMapping("/blogs/add")
    public String addBlog(@ModelAttribute Blog blog, @RequestParam("imageFile") MultipartFile imageFile) {
        String imagePath = saveImage(imageFile);
        if (imagePath != null) {
            blog.setBlogImagePath(imagePath);
        }
        blog.setPublishedDate(LocalDateTime.now());
        blogRepository.save(blog);
        return "redirect:/admin/blogs";
    }

    @PostMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogRepository.deleteById(id);
        return "redirect:/admin/blogs";
    }

    // Course management
    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/manage-courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute Course course) {
        courseRepository.save(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "redirect:/admin/courses";
    }

    // Fee management
    @GetMapping("/fees")
    public String manageFees(Model model) {
        model.addAttribute("users", userRepository.findAll()); // Assuming users are students for simplicity
        return "admin/fees";
    }

    @PostMapping("/fees/update")
    public String updateFee(@RequestParam Long userId, @RequestParam Double amount) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPendingFees(amount);
            userRepository.save(user);
        });
        return "redirect:/admin/fees";
    }

    // Admission List
    @GetMapping("/admissions")
    public String viewAdmissions(Model model) {
        model.addAttribute("admissions", admissionRepository.findAll());
        return "admin/admissions";
    }

    @GetMapping("/students")
    public String manageStudents(Model model) {
        model.addAttribute("students", userRepository.findAllByRoleName("ROLE_USER"));
        return "admin/manage-students";
    }

    @GetMapping("/students/register")
    public String directRegistration(Model model) {
        return "admin/direct-registration";
    }

    @PostMapping("/admissions/admit/{id}")
    public String admitStudent(@PathVariable Long id) {
        Optional<AdmissionApplication> applicationOpt = admissionRepository.findById(id);
        if (applicationOpt.isPresent()) {
            AdmissionApplication app = applicationOpt.get();
            if ("PENDING".equals(app.getAdmissionStatus())) {
                // Generate next roll number
                String maxRoll = userRepository.findMaxRollNumber();
                String nextRoll = generateNextRollNumber(maxRoll);

                // Create User Account
                User student = new User();
                student.setUsername(nextRoll);
                student.setRollNumber(nextRoll);
                student.setFullName(app.getFirstName() + " " + app.getLastName());
                student.setDateOfBirth(app.getDob());
                student.setEmail(app.getEmail());
                student.setPhone(app.getFatherPhone());
                student.setPendingFees(0.0); // Reset or set default
                student.setSession(app.getSession());
                
                // Set password as DOB (remove slashes/dashes if any, or use as is)
                String rawPassword = app.getDob().replaceAll("[^0-9]", ""); // 01012015
                if (rawPassword.isEmpty()) rawPassword = "password123"; // Fallback
                student.setPassword(passwordEncoder.encode(rawPassword));

                // Assign ROLE_USER (Student)
                Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_USER");
                        return roleRepository.save(r);
                    });
                student.setRoles(new HashSet<>(Collections.singletonList(userRole)));

                userRepository.save(student);

                // Update application status
                app.setAdmissionStatus("ADMITTED");
                admissionRepository.save(app);
            }
        }
        return "redirect:/admin/admissions";
    }

    @PostMapping("/students/register")
    public String registerStudent(@RequestParam String fullName, @RequestParam String dob, 
                                @RequestParam(required = false) String email, 
                                @RequestParam(required = false) String phone,
                                @RequestParam String session) {
        
        // Generate next roll number
        String maxRoll = userRepository.findMaxRollNumber();
        String nextRoll = generateNextRollNumber(maxRoll);

        // Create User Account
        User student = new User();
        student.setUsername(nextRoll);
        student.setRollNumber(nextRoll);
        student.setFullName(fullName);
        student.setDateOfBirth(dob);
        student.setEmail(email);
        student.setPhone(phone);
        student.setPendingFees(0.0);
        student.setSession(session);
        
        // Password = DOB (digits only)
        String rawPassword = dob.replaceAll("[^0-9]", "");
        if (rawPassword.isEmpty()) rawPassword = "password123";
        student.setPassword(passwordEncoder.encode(rawPassword));

        // Assign ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_USER");
                return roleRepository.save(r);
            });
        student.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        userRepository.save(student);

        return "redirect:/admin/students?success";
    }

    private String generateNextRollNumber(String maxRoll) {
        if (maxRoll == null || !maxRoll.startsWith("IPSM")) {
            return "IPSM0001";
        }
        try {
            int num = Integer.parseInt(maxRoll.substring(4));
            return String.format("IPSM%04d", num + 1);
        } catch (NumberFormatException e) {
            return "IPSM0001";
        }
    }

    // Helper method for image upload
    private String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) return null;
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/uploads/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
