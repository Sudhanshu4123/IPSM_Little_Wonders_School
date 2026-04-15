package com.littlewonders.controller;

import com.littlewonders.model.*;
import com.littlewonders.config.FeeConfig;
import com.littlewonders.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final String UPLOAD_DIR = "external-uploads/";

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private CelebrationRepository celebrationRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SecurityAuditRepository securityAuditRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EnquiryRepository enquiryRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private PerformanceReportRepository performanceReportRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private CareerRepository careerRepository;
    @Autowired
    private FeeTransactionRepository feeTransactionRepository;
    @Autowired
    private FeeConfig feeConfig;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", userRepository.findAllByRoleName("ROLE_USER").size());
        model.addAttribute("totalBlogs", blogRepository.count());
        model.addAttribute("totalCourses", courseRepository.count());
        model.addAttribute("totalCelebrations", celebrationRepository.count());
        model.addAttribute("totalBranches", branchRepository.count());
        model.addAttribute("totalGalleryItems", galleryRepository.count());
        model.addAttribute("securityLogs", securityAuditRepository.findTop10ByOrderByAttemptedAtDesc());
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    // Branch management
    @GetMapping("/branches")
    public String manageBranches(Model model) {
        model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("activePage", "branches");
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
        model.addAttribute("activePage", "gallery");
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
        model.addAttribute("activePage", "celebrations");
        return "admin/manage-celebrations";
    }

    @PostMapping("/celebrations/add")
    public String addCelebration(@ModelAttribute Celebration celebration,
            @RequestParam("imageFile") MultipartFile imageFile) {
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
        model.addAttribute("activePage", "blogs");
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
        model.addAttribute("activePage", "courses");
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
        model.addAttribute("users", userRepository.findAllByRoleName("ROLE_USER")); // Only students, exclude admin
        model.addAttribute("activePage", "fees");
        model.addAttribute("feeConfig", feeConfig);
        return "admin/fees";
    }

    @GetMapping("/fees/summary")
    public String feeSummary(Model model) {
        java.util.List<User> students = userRepository.findAllByRoleName("ROLE_USER");
        double totalPending = students.stream().mapToDouble(u -> u.getPendingFees() != null ? u.getPendingFees() : 0.0)
                .sum();
        long studentsWithDues = students.stream().filter(u -> u.getPendingFees() != null && u.getPendingFees() > 0)
                .count();

        model.addAttribute("totalPending", totalPending);
        model.addAttribute("studentsWithDues", studentsWithDues);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("users", students);
        model.addAttribute("activePage", "fee-summary");
        return "admin/fee-summary";
    }

    @GetMapping("/fees/summary/session")
    public String feeSummaryBySession(@RequestParam String session,
                                    @RequestParam(required = false) String courseName,
                                    Model model) {
        List<FeeTransaction> allTxs = feeTransactionRepository.findAll();
        
        java.util.Map<User, Double> studentPayments = allTxs.stream()
            .filter(t -> {
                boolean matchesSession = (session == null || session.isEmpty() || session.equalsIgnoreCase("All")) || 
                                       (t.getStudent().getSession() != null && t.getStudent().getSession().equalsIgnoreCase(session));
                boolean matchesCourse = (courseName == null || courseName.isEmpty() || courseName.equalsIgnoreCase("All")) || 
                                       (t.getStudent().getCourse() != null && t.getStudent().getCourse().equalsIgnoreCase(courseName));
                return matchesSession && matchesCourse;
            })
            .collect(Collectors.groupingBy(
                FeeTransaction::getStudent,
                Collectors.summingDouble(FeeTransaction::getAmount)
            ));

        model.addAttribute("paidData", studentPayments);
        model.addAttribute("reportTitle", "Session Wise Report (" + session + ")");
        model.addAttribute("courseName", courseName);
        model.addAttribute("activePage", "fee-session");
        return "admin/fee-report-result";
    }

    @GetMapping("/fees/summary/month")
    public String feeSummaryByMonth(@RequestParam String month,
                                  @RequestParam String year,
                                  @RequestParam(required = false) String courseName,
                                  Model model) {
        List<FeeTransaction> allTxs = feeTransactionRepository.findAll();
        
        java.util.Map<User, Double> studentPayments = allTxs.stream()
            .filter(t -> {
                boolean matchesMonth = (month == null || month.isEmpty() || month.equalsIgnoreCase("All")) || 
                                     (t.getMonth() != null && t.getMonth().equalsIgnoreCase(month));
                boolean matchesYear = (year == null || year.isEmpty() || year.equalsIgnoreCase("All")) || 
                                    (t.getPaymentDate().getYear() == Integer.parseInt(year));
                boolean matchesCourse = (courseName == null || courseName.isEmpty() || courseName.equalsIgnoreCase("All")) || 
                                       (t.getStudent().getCourse() != null && t.getStudent().getCourse().equalsIgnoreCase(courseName));
                return matchesMonth && matchesYear && matchesCourse;
            })
            .collect(Collectors.groupingBy(
                FeeTransaction::getStudent,
                Collectors.summingDouble(FeeTransaction::getAmount)
            ));

        model.addAttribute("paidData", studentPayments);
        model.addAttribute("reportTitle", "Month Wise Report (" + month + " " + year + ")");
        model.addAttribute("courseName", courseName);
        model.addAttribute("activePage", "fee-summary");
        return "admin/fee-report-result";
    }

    @GetMapping("/fees/summary/date")
    public String feeSummaryByDate(@RequestParam String fromDate, 
                                 @RequestParam String toDate,
                                 @RequestParam(required = false) String courseName,
                                 Model model) {
        java.time.LocalDate start = java.time.LocalDate.parse(fromDate);
        java.time.LocalDate end = java.time.LocalDate.parse(toDate);
        
        List<FeeTransaction> allTxs = feeTransactionRepository.findAll();
        
        java.util.Map<User, Double> studentPayments = allTxs.stream()
            .filter(t -> {
                java.time.LocalDate pDate = t.getPaymentDate().toLocalDate();
                boolean inRange = !pDate.isBefore(start) && !pDate.isAfter(end);
                boolean matchesCourse = (courseName == null || courseName.isEmpty() || courseName.equalsIgnoreCase("All")) || 
                                       (t.getStudent().getCourse() != null && t.getStudent().getCourse().equalsIgnoreCase(courseName));
                return inRange && matchesCourse;
            })
            .collect(Collectors.groupingBy(
                FeeTransaction::getStudent,
                Collectors.summingDouble(FeeTransaction::getAmount)
            ));

        model.addAttribute("paidData", studentPayments);
        model.addAttribute("reportTitle", "Date Wise Report (" + fromDate + " to " + toDate + ")");
        model.addAttribute("courseName", courseName);
        model.addAttribute("activePage", "fee-date");
        return "admin/fee-report-result";
    }

    @GetMapping("/fees/summary/student")
    public String studentFeeSummary(@RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) String month,
            Model model) {
        model.addAttribute("students", userRepository.findAllByRoleName("ROLE_USER"));
        if (studentId != null) {
            userRepository.findById(studentId).ifPresent(student -> {
                model.addAttribute("selectedStudent", student);
                List<FeeTransaction> txs = feeTransactionRepository.findByStudent(student);

                // Detailed Filter Logic
                if (feeType != null && !feeType.isEmpty() && !feeType.equalsIgnoreCase("All")) {
                    txs = txs.stream().filter(t -> t.getFeeType().toLowerCase().contains(feeType.toLowerCase())).collect(Collectors.toList());
                }
                if (month != null && !month.isEmpty() && !month.equalsIgnoreCase("All")) {
                    txs = txs.stream().filter(t -> t.getMonth() != null && t.getMonth().equalsIgnoreCase(month)).collect(Collectors.toList());
                }

                model.addAttribute("transactions", txs);

                // --- NEW: Calculate Due Fees Checklist ---
                java.util.Map<String, String> feeStatus = new java.util.LinkedHashMap<>();
                String[] oneTimeFees = { "Registration Fee", "Admission Fee", "Annual Fee", "Uniform Fee", "Books",
                        "Bag Fee", "ID Card Fee", "Stationary Fee", "Festival & Birthday Celebration",
                        "Picnic & Extra Curricular Activities", "Yoga & Music & Dance" };

                // Get month for status check (either filtered or current)
                String currentMonth = new java.text.SimpleDateFormat("MMMM").format(new java.util.Date());
                String monthToCheck = (month != null && !month.isEmpty() && !month.equals("All")) ? month
                        : currentMonth;

                for (String fee : oneTimeFees) {
                    boolean isPaid = feeTransactionRepository.findByStudent(student).stream()
                            .anyMatch(t -> t.getFeeType().contains(fee));
                    feeStatus.put(fee, isPaid ? "PAID" : "PENDING");
                }

                // Tuition Fee Check (Filtered Month)
                boolean tuitionPaid = feeTransactionRepository.findByStudent(student).stream()
                        .anyMatch(t -> t.getFeeType().contains("Tution Fee")
                                && monthToCheck.equalsIgnoreCase(t.getMonth()));
                feeStatus.put("Tution Fee (" + monthToCheck + ")", tuitionPaid ? "PAID" : "PENDING");

                model.addAttribute("feeStatus", feeStatus);
                model.addAttribute("selectedMonth", monthToCheck);
            });
        }
        model.addAttribute("activePage", "fee-student");
        return "admin/fee-student-summary";
    }

    @GetMapping("/fees/receipt/{txId}")
    public String printReceipt(@PathVariable Long txId, Model model) {
        feeTransactionRepository.findById(txId).ifPresent(tx -> {
            model.addAttribute("transaction", tx);
            model.addAttribute("student", tx.getStudent());
        });
        return "admin/print-receipt";
    }

    @PostMapping("/fees/update")
    @Transactional
    public String updateFee(@RequestParam Long userId, @RequestParam Double amount,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String paymentMode) {
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return "redirect:/admin/fees?error";
        }

        User user = userOpt.get();
        FeeTransaction transaction = new FeeTransaction();
        transaction.setStudent(user);
        transaction.setFeeType(feeType != null ? feeType : "Fee Payment");
        transaction.setMonth(month != null ? month : "N/A");
        transaction.setYear("2024-25");
        transaction.setAmount(amount);
        transaction.setPaymentMode(paymentMode != null ? paymentMode : "Cash");
        transaction.setPaymentDate(LocalDateTime.now());
        
        FeeTransaction saved = feeTransactionRepository.save(transaction);

        Double currentPending = user.getPendingFees() != null ? user.getPendingFees() : 0.0;
        user.setPendingFees(Math.max(0.0, currentPending - amount));
        userRepository.save(user);
        
        return "redirect:/admin/fees?success=true&txId=" + saved.getId();
    }

    @GetMapping("/fees/receipts")
    public String generateReceiptList(Model model) {
        List<User> students = userRepository.findAllByRoleName("ROLE_USER");
        java.util.Map<Long, FeeTransaction> latestTransactions = new java.util.HashMap<>();
        
        for (User student : students) {
            List<FeeTransaction> txs = feeTransactionRepository.findByStudentOrderByPaymentDateDesc(student);
            if (!txs.isEmpty()) {
                latestTransactions.put(student.getId(), txs.get(0));
            }
        }
        
        model.addAttribute("students", students);
        model.addAttribute("latestTransactions", latestTransactions);
        model.addAttribute("activePage", "receipts");
        return "admin/generate-receipt-list";
    }

    // Admission List
    @GetMapping("/admissions")
    public String viewAdmissions(Model model) {
        model.addAttribute("admissions", admissionRepository.findAll());
        model.addAttribute("activePage", "admissions");
        return "admin/admissions";
    }

    @GetMapping("/admissions/download/{id}")
    public String downloadAdmissionForm(@PathVariable Long id, Model model) {
        admissionRepository.findById(id).ifPresent(app -> {
            model.addAttribute("app", app);
        });
        return "admin/admission-form-download";
    }

    @GetMapping("/students")
    public String manageStudents(Model model) {
        model.addAttribute("students", userRepository.findAllByRoleName("ROLE_USER"));
        model.addAttribute("activePage", "active-students");
        return "admin/manage-students";
    }

    @GetMapping("/students/profile/{id}")
    public String studentProfile(@PathVariable Long id, Model model) {
        userRepository.findById(id).ifPresent(student -> {
            model.addAttribute("student", student);
        });
        model.addAttribute("activePage", "active-students");
        return "admin/student-profile";
    }

    @GetMapping("/students/icards")
    public String icardPage(Model model) {
        model.addAttribute("students", userRepository.findAllByRoleName("ROLE_USER"));
        model.addAttribute("activePage", "icards");
        return "admin/student-icard";
    }

    @GetMapping("/students/register")
    public String directRegistration(Model model) {
        model.addAttribute("activePage", "registration");
        model.addAttribute("feeConfig", feeConfig);
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
                student.setSession(app.getSession());
                student.setCourse(app.getProgramApplied());

                // Initialize Document/Fee Info from centralized Config
                student.setRegistrationFee(feeConfig.getRegistrationFee());
                student.setAdmissionFee(feeConfig.getAdmissionFee());
                student.setAnnualFee(feeConfig.getAnnualFee());
                student.setUniformFee(feeConfig.getUniformFee());
                student.setBooksFee(feeConfig.getBooksFee());
                student.setBagFee(feeConfig.getBagFee());
                student.setIdCardFee(feeConfig.getIdCardFee());
                student.setStationaryFee(feeConfig.getStationaryFee());
                student.setCelebrationFee(feeConfig.getCelebrationFee());
                student.setActivityFee(feeConfig.getActivityFee());
                student.setYogaMusicDanceFee(feeConfig.getYogaMusicDanceFee());

                double tuitionFee = feeConfig.getTuitionFee(app.getProgramApplied());
                student.setTuitionFee(tuitionFee);

                double oneTimeFees = student.getRegistrationFee() + student.getAdmissionFee() + student.getAnnualFee()
                        + student.getUniformFee() + student.getBooksFee() + student.getBagFee() + student.getIdCardFee()
                        + student.getStationaryFee() + student.getCelebrationFee() + student.getActivityFee()
                        + student.getYogaMusicDanceFee();
                student.setPendingFees(oneTimeFees + student.getTuitionFee());

                // Detailed student info from application
                student.setFatherName(app.getFatherName());
                student.setMotherName(app.getMotherName());
                student.setFatherPhone(app.getFatherPhone());
                student.setMotherPhone(app.getMotherPhone());
                student.setAlternatePhone(app.getAlternatePhone());
                student.setAddress(app.getAddress());
                student.setGender(app.getGender());
                student.setBloodGroup(app.getBloodGroup());
                student.setAge(app.getAge());
                student.setFatherOccupation(app.getFatherOccupation());
                student.setMotherOccupation(app.getMotherOccupation());
                student.setTotalChildren(app.getTotalChildren());

                // Password as DOB (digits only)
                String rawPassword = app.getDob().replaceAll("[^0-9]", "");
                if (rawPassword.isEmpty())
                    rawPassword = "password123";
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
            @RequestParam String session,
            @RequestParam String course,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String fatherName,
            @RequestParam(required = false) String motherName,
            @RequestParam(required = false) String motherPhone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer totalChildren,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String fatherOccupation,
            @RequestParam(required = false) String motherOccupation,
            @RequestParam(required = false) MultipartFile childPhoto,
            @RequestParam(required = false) MultipartFile fatherPhoto,
            @RequestParam(required = false) MultipartFile motherPhoto,
            @RequestParam(required = false) MultipartFile guardianPhoto,
            @RequestParam(required = false) MultipartFile birthCertificate,
            @RequestParam(required = false) MultipartFile aadharCard,
            @RequestParam(required = false) MultipartFile panCard,
            @RequestParam(required = false) Double registrationFee,
            @RequestParam(required = false) Double admissionFee,
            @RequestParam(required = false) Double annualFee,
            @RequestParam(required = false) Double tuitionFee,
            @RequestParam(required = false) Double uniformFee,
            @RequestParam(required = false) Double booksFee,
            @RequestParam(required = false) Double bagFee,
            @RequestParam(required = false) Double idCardFee,
            @RequestParam(required = false) Double stationaryFee,
            @RequestParam(required = false) Double celebrationFee,
            @RequestParam(required = false) Double activityFee,
            @RequestParam(required = false) Double yogaMusicDanceFee) {

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
        student.setSession(session);
        student.setCourse(course);

        // New Detailed Info
        student.setGender(gender);
        student.setFatherName(fatherName);
        student.setFatherPhone(phone);
        student.setMotherName(motherName);
        student.setMotherPhone(motherPhone);
        student.setAddress(address);
        student.setTotalChildren(totalChildren);
        student.setBloodGroup(bloodGroup);
        student.setAge(age);
        student.setFatherOccupation(fatherOccupation);
        student.setMotherOccupation(motherOccupation);

        // Handle File Uploads
        if (childPhoto != null && !childPhoto.isEmpty()) {
            student.setChildPhotoPath(saveImage(childPhoto));
        }
        if (fatherPhoto != null && !fatherPhoto.isEmpty()) {
            student.setFatherPhotoPath(saveImage(fatherPhoto));
        }
        if (motherPhoto != null && !motherPhoto.isEmpty()) {
            student.setMotherPhotoPath(saveImage(motherPhoto));
        }
        if (guardianPhoto != null && !guardianPhoto.isEmpty()) {
            student.setGuardianPhotoPath(saveImage(guardianPhoto));
        }
        if (birthCertificate != null && !birthCertificate.isEmpty()) {
            student.setBirthCertificatePath(saveImage(birthCertificate));
        }
        if (aadharCard != null && !aadharCard.isEmpty()) {
            student.setAadharCardPath(saveImage(aadharCard));
        }
        if (panCard != null && !panCard.isEmpty()) {
            student.setPanCardPath(saveImage(panCard));
        }

        // Fee Details Processing - Fallback to FeeConfig if RequestParams are null
        student.setRegistrationFee(registrationFee != null ? registrationFee : feeConfig.getRegistrationFee());
        student.setAdmissionFee(admissionFee != null ? admissionFee : feeConfig.getAdmissionFee());
        student.setAnnualFee(annualFee != null ? annualFee : feeConfig.getAnnualFee());

        student.setTuitionFee(tuitionFee != null ? tuitionFee : feeConfig.getTuitionFee(course));
        student.setUniformFee(uniformFee != null ? uniformFee : feeConfig.getUniformFee());
        student.setBooksFee(booksFee != null ? booksFee : feeConfig.getBooksFee());
        student.setBagFee(bagFee != null ? bagFee : feeConfig.getBagFee());
        student.setIdCardFee(idCardFee != null ? idCardFee : feeConfig.getIdCardFee());
        student.setStationaryFee(stationaryFee != null ? stationaryFee : feeConfig.getStationaryFee());
        student.setCelebrationFee(celebrationFee != null ? celebrationFee : feeConfig.getCelebrationFee());
        student.setActivityFee(activityFee != null ? activityFee : feeConfig.getActivityFee());
        student.setYogaMusicDanceFee(yogaMusicDanceFee != null ? yogaMusicDanceFee : feeConfig.getYogaMusicDanceFee());

        // Calculate Initial Dues: Sum of provided/default fees
        double oneTimeFees = student.getRegistrationFee() + student.getAdmissionFee() + student.getAnnualFee()
                + student.getUniformFee() + student.getBooksFee() + student.getBagFee() + student.getIdCardFee()
                + student.getStationaryFee() + student.getCelebrationFee() + student.getActivityFee()
                + student.getYogaMusicDanceFee();

        student.setPendingFees(oneTimeFees + student.getTuitionFee());

        // Password = DOB (digits only)
        String rawPassword = dob.replaceAll("[^0-9]", "");
        if (rawPassword.isEmpty())
            rawPassword = "password123";
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

        return "redirect:/admin/students/register?success";
    }

    @Transactional
    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(student -> {
            // Delete associated records first to avoid foreign key constraints
            attendanceRepository.deleteByStudent(student);
            feeTransactionRepository.deleteByStudent(student);
            performanceReportRepository.deleteByStudent(student);

            // Now delete the user
            userRepository.delete(student);
        });
        return "redirect:/admin/students";
    }

    // Enquiry Management
    @GetMapping("/enquiries")
    public String viewEnquiries(Model model) {
        model.addAttribute("enquiries", enquiryRepository.findAll());
        model.addAttribute("activePage", "enquiries");
        return "admin/manage-enquiries";
    }

    @PostMapping("/enquiries/delete/{id}")
    public String deleteEnquiry(@PathVariable Long id) {
        enquiryRepository.deleteById(id);
        return "redirect:/admin/enquiries";
    }

    // Franchise Enquiry Management
    @GetMapping("/franchise-enquiries")
    public String viewFranchiseEnquiries(Model model) {
        model.addAttribute("franchiseEnquiries", franchiseRepository.findAll());
        model.addAttribute("activePage", "franchise-enquiries");
        return "admin/manage-franchise-enquiries";
    }

    @PostMapping("/franchise-enquiries/delete/{id}")
    public String deleteFranchiseEnquiry(@PathVariable Long id) {
        franchiseRepository.deleteById(id);
        return "redirect:/admin/franchise-enquiries";
    }

    // Career Submission Management
    @GetMapping("/career-submissions")
    public String viewCareerSubmissions(Model model) {
        model.addAttribute("careerSubmissions", careerRepository.findAll());
        model.addAttribute("activePage", "career-submissions");
        return "admin/manage-career-submissions";
    }

    @PostMapping("/career-submissions/delete/{id}")
    public String deleteCareerSubmission(@PathVariable Long id) {
        careerRepository.deleteById(id);
        return "redirect:/admin/career-submissions";
    }

    // Attendance Management
    @GetMapping("/attendance")
    public String manageAttendance(Model model, @RequestParam(required = false) String session) {
        if (session != null && !session.isEmpty()) {
            model.addAttribute("students", userRepository.findAllBySessionAndRoles_Name(session, "ROLE_USER"));
        }
        model.addAttribute("session", session);
        model.addAttribute("activePage", "attendance");
        return "admin/manage-attendance";
    }

    @PostMapping("/attendance/submit")
    public String submitAttendance(@RequestParam String date, @RequestParam String session,
            @RequestParam java.util.Map<String, String> attendanceData) {
        java.time.LocalDate localDate = java.time.LocalDate.parse(date);
        attendanceData.forEach((key, value) -> {
            if (key.startsWith("status_")) {
                Long studentId = Long.parseLong(key.replace("status_", ""));
                userRepository.findById(studentId).ifPresent(student -> {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setDate(localDate);
                    attendance.setStatus(value);
                    attendanceRepository.save(attendance);
                });
            }
        });
        return "redirect:/admin/attendance?success&session=" + session;
    }

    // Exam Management
    @GetMapping("/exams")
    public String manageExams(Model model) {
        model.addAttribute("exams", examRepository.findAll());
        model.addAttribute("activePage", "exams");
        return "admin/manage-exams";
    }

    @PostMapping("/exams/add")
    public String addExam(@ModelAttribute Exam exam) {
        examRepository.save(exam);
        return "redirect:/admin/exams";
    }

    @PostMapping("/exams/delete/{id}")
    public String deleteExam(@PathVariable Long id) {
        examRepository.deleteById(id);
        return "redirect:/admin/exams";
    }

    // Performance Report Management
    @GetMapping("/performance")
    public String managePerformance(Model model, @RequestParam(required = false) Long examId) {
        model.addAttribute("exams", examRepository.findAll());
        if (examId != null) {
            model.addAttribute("reports",
                    performanceReportRepository.findByExam(examRepository.findById(examId).orElse(null)));
            model.addAttribute("selectedExamId", examId);
        }
        model.addAttribute("activePage", "performance");
        return "admin/manage-performance";
    }

    @GetMapping("/performance/add")
    public String addPerformanceForm(Model model, @RequestParam Long examId) {
        model.addAttribute("exam", examRepository.findById(examId).orElseThrow());
        model.addAttribute("students", userRepository.findAllByRoleName("ROLE_USER"));
        model.addAttribute("activePage", "performance");
        return "admin/add-performance";
    }

    @PostMapping("/performance/add")
    public String addPerformance(@ModelAttribute PerformanceReport report, @RequestParam Long examId,
            @RequestParam Long studentId) {
        report.setExam(examRepository.findById(examId).orElseThrow());
        report.setStudent(userRepository.findById(studentId).orElseThrow());
        performanceReportRepository.save(report);
        return "redirect:/admin/performance?examId=" + examId;
    }

    // I-Card Generation (Single)
    @GetMapping("/students/icard/{id}")
    public String generateICard(@PathVariable Long id, Model model) {
        Optional<User> studentOpt = userRepository.findById(id);
        if (studentOpt.isPresent()) {
            model.addAttribute("students", Collections.singletonList(studentOpt.get()));
        } else {
            model.addAttribute("students", Collections.emptyList());
        }
        return "admin/print-icard";
    }

    // Bulk I-Card Generation
    @GetMapping("/students/icards/bulk")
    public String generateBulkICards(@RequestParam(required = false) String fromRoll,
            @RequestParam(required = false) String toRoll,
            Model model) {
        if (fromRoll == null || toRoll == null || fromRoll.isEmpty() || toRoll.isEmpty()) {
            return "redirect:/admin/students/icards?error=Select%20Roll%20Numbers";
        }

        List<User> allStudents = userRepository.findAllByRoleName("ROLE_USER");
        List<User> filteredStudents = allStudents.stream()
                .filter(s -> s.getRollNumber() != null &&
                        s.getRollNumber().compareToIgnoreCase(fromRoll) >= 0 &&
                        s.getRollNumber().compareToIgnoreCase(toRoll) <= 0)
                .sorted((s1, s2) -> s1.getRollNumber().compareToIgnoreCase(s2.getRollNumber()))
                .collect(Collectors.toList());

        if (filteredStudents.isEmpty()) {
            return "redirect:/admin/students/icards?error=No%20students%20found%20in%20selected%20range";
        }

        model.addAttribute("students", filteredStudents);
        return "admin/print-icard";
    }

    private String generateNextRollNumber(String maxRoll) {
        if (maxRoll == null || !maxRoll.startsWith("IPSM")) {
            return "IPSM1001";
        }
        try {
            int num = Integer.parseInt(maxRoll.substring(4));
            // If the current highest is lower than 1000, start from 1001
            if (num < 1000) {
                return "IPSM1001";
            }
            return String.format("IPSM%04d", num + 1);
        } catch (NumberFormatException e) {
            return "IPSM1001";
        }
    }

    // Helper method for image upload
    private String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty())
            return null;
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
