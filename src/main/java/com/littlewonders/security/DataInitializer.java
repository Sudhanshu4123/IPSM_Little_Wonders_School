package com.littlewonders.security;

import com.littlewonders.model.Role;
import com.littlewonders.model.User;
import com.littlewonders.repository.RoleRepository;
import com.littlewonders.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final com.littlewonders.repository.CourseRepository courseRepository;
    private final com.littlewonders.repository.CelebrationRepository celebrationRepository;
    private final com.littlewonders.repository.BranchRepository branchRepository;
    private final com.littlewonders.repository.GalleryRepository galleryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, 
                           com.littlewonders.repository.CourseRepository courseRepository, 
                           com.littlewonders.repository.CelebrationRepository celebrationRepository,
                           com.littlewonders.repository.BranchRepository branchRepository,
                           com.littlewonders.repository.GalleryRepository galleryRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.celebrationRepository = celebrationRepository;
        this.branchRepository = branchRepository;
        this.galleryRepository = galleryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Initializing Courses
        initializeCourse("Toddlers", "1 Year", 5000.0, "Nurturing environment for toddlers.");
        initializeCourse("Pre-Nursery", "1 Year", 6000.0, "Play-based learning for pre-nursery.");
        initializeCourse("Nursery", "1 Year", 7000.0, "Building strong foundations in Nursery.");
        initializeCourse("Prep", "1 Year", 8000.0, "Preparing little wonders for primary school.");

        // Initializing Branches if none exist
        if (branchRepository.count() == 0) {
            initializeBranch("Main Branch", "123 Education Lane, Delhi", "9876543210", "main@littlewonders.com", 
                             "Our flagship branch with premium facilities, spacious classrooms, and a multi-purpose play area.", 
                             "/images/hero_preschool.png");
        }

        // Initializing Sample Gallery if none exist
        if (galleryRepository.count() == 0) {
            initializeGallery("Classroom Fun", "/images/hero_preschool.png", "Classroom");
            initializeGallery("Learning Environment", "/images/hero_preschool.png", "Classroom");
            initializeGallery("Annual Meet", "/images/hero_preschool.png", "Event");
            initializeGallery("Kids Activities", "/images/hero_preschool.png", "Others");
        }

        // Initializing All Celebrations from Website
        initializeCelebration("Independence Day", "Celebrating India's independence.", "/images/INDEPENDENCE DAY.png");
        initializeCelebration("Republic Day", "Celebrating the Republic of India.", "/images/REPUBLIC DAY.png");
        initializeCelebration("Sports Day", "Annual sports meet.", "/images/SPORTS DAY.png");
        initializeCelebration("Earth Day", "Protecting our planet.", "/images/EARTH DAY.png");
        initializeCelebration("Annual Day", "Annual school function.", "/images/ANNUAL DAY.png");
        initializeCelebration("World Animal Day", "Love for animals.", "/images/WORLD ANIMAL DAY.png");
        initializeCelebration("World Happiness Day", "Spreading joy.", "/images/INTERNATIONAL DAY OF HAPPINESS.png");
        initializeCelebration("Teachers Day", "Honoring our mentors.", "/images/TEACHERS DAY.png");
        initializeCelebration("Children's Day", "Celebrating childhood.", "/images/CHILDRENS DAY.png");
        initializeCelebration("Birthday", "Special day for our little wonders.", "/images/BIRTHDAY.png");
        initializeCelebration("Grand Parents Day", "Honoring elders.", "/images/GRAND PARENTS DAY.png");
        initializeCelebration("Father's Day", "Celebrating fathers.", "/images/FATHERS DAY.png");
        initializeCelebration("Mother's Day", "Honoring mothers.", "/images/MOTHERS DAY.png");
        initializeCelebration("Siblings Day", "Brother-sister bond.", "/images/SIBLINGS DAY.png");
        initializeCelebration("Friendship Day", "Best friends forever.", "/images/FRIENDSHIP DAY.png");
        initializeCelebration("Holi", "Festival of colors.", "/images/HOLI.png");
        initializeCelebration("Diwali", "Festival of lights.", "/images/DIWALI.png");
        initializeCelebration("Eid", "Celebrating Eid.", "/images/EID.png");
        initializeCelebration("Christmas", "Season of joy.", "/images/CHRISTMAS.png");
        initializeCelebration("Guru Nanak Jayanti", "Shri Guru Nanak Birthday.", "/images/GURU NANAK JAYANTI.png");
        initializeCelebration("Budhh Purnima", "Celebrating Lord Buddha.", "/images/BUDHH PURNIMA.png");
        initializeCelebration("Graduation Day", "Preschool graduation.", "/images/GRADUATION DAY.png");
        initializeCelebration("Art & Craft Day", "Creative expression.", "/images/ART & CRAFT DAY.png");
        initializeCelebration("Picnic Day", "Outdoor fun.", "/images/PICNIC DAY.png");
        initializeCelebration("Vegetable Day", "Learning about healthy food.", "/images/VEGETABLE DAY.png");
        initializeCelebration("Fruit Day", "Nature's sweet treats.", "/images/FRUIT DAY.png");
        initializeCelebration("Colors Day", "World of colors.", "/images/COLORS DAY.png");
        initializeCelebration("Talent Hunt Day", "Showcasing unique skills.", "/images/TALENT HUNT DAY.png");
        initializeCelebration("Fancy Dress Day", "Creative dressing.", "/images/FANCY DRESS.png");
        initializeCelebration("Space/Astronaut Day", "Exploring the universe.", "/images/SPACE_ ASTRONAUT DAY.png");
        initializeCelebration("Magic Show", "World of illusions.", "/images/MAGIC SHOW DAY.png");
        initializeCelebration("Puppet Theatre", "Traditional storytelling.", "/images/PUPPET THEATRE DAY.png");

        // Initializing Roles
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Clean up old 'admin' user if it exists and is different from the new one
        if (!adminUsername.equals("admin")) {
            userRepository.findByUsername("admin").ifPresent(userRepository::delete);
        }

        // Creating / Updating Secure Admin from configuration
        userRepository.findByUsername(adminUsername).ifPresentOrElse(
            admin -> {
                admin.setPassword(passwordEncoder.encode(adminPassword));
                userRepository.save(admin);
                System.out.println("Admin updated from configuration: " + adminUsername);
            },
            () -> {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFullName("School Administrator");
                Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
                admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
                userRepository.save(admin);
                System.out.println("Secure admin created from configuration: " + adminUsername);
            }
        );
    }

    private void initializeGallery(String title, String url, String category) {
        com.littlewonders.model.GalleryItem item = new com.littlewonders.model.GalleryItem();
        item.setTitle(title);
        item.setImageUrl(url);
        item.setCategory(category);
        galleryRepository.save(item);
    }

    private void initializeBranch(String name, String address, String contact, String email, String description, String imagePath) {
        if (branchRepository.findByName(name).isEmpty()) {
            com.littlewonders.model.Branch branch = new com.littlewonders.model.Branch();
            branch.setName(name);
            branch.setAddress(address);
            branch.setContactNumber(contact);
            branch.setEmail(email);
            branch.setDescription(description);
            branch.setImagePath(imagePath);
            branchRepository.save(branch);
        }
    }

    private void initializeCourse(String name, String duration, Double fee, String description) {
        if (courseRepository.findByName(name).isEmpty()) {
            com.littlewonders.model.Course course = new com.littlewonders.model.Course();
            course.setName(name);
            course.setDuration(duration);
            course.setStandardFee(fee);
            course.setDescription(description);
            courseRepository.save(course);
        }
    }

    private void initializeCelebration(String title, String description, String imagePath) {
        if (celebrationRepository.findByTitle(title).isEmpty()) {
            com.littlewonders.model.Celebration celebration = new com.littlewonders.model.Celebration();
            celebration.setTitle(title);
            celebration.setDescription(description);
            celebration.setImagePath(imagePath);
            celebration.setEventDate(java.time.LocalDateTime.now());
            celebration.setPublishedDate(java.time.LocalDateTime.now());
            celebrationRepository.save(celebration);
        }
    }
}
