// Preloader logic
window.addEventListener('load', () => {
    const loader = document.querySelector('.loader-wrapper');
    if (loader) {
        loader.classList.add('fade-out');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    // Reveal animations on scroll
    const reveals = document.querySelectorAll('.animate-up');

    const reveal = () => {
        reveals.forEach(element => {
            const windowHeight = window.innerHeight;
            const elementTop = element.getBoundingClientRect().top;
            const elementVisible = 150;

            if (elementTop < windowHeight - elementVisible) {
                element.classList.add('active');
            }
        });
    };

    window.addEventListener('scroll', reveal);
    reveal(); // Initial check

    // Header Scroll Effect
    const header = document.querySelector('header');
    const handleScroll = () => {
        if (window.scrollY > 40) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    };
    window.addEventListener('scroll', handleScroll);
    handleScroll(); // Initial check

    // Dropdown toggle on click
    const dropdowns = document.querySelectorAll('.dropdown');

    dropdowns.forEach(dropdown => {
        const link = dropdown.querySelector('a');
        if (link) {
            link.addEventListener('click', (e) => {
                if (window.innerWidth <= 992) {
                    e.preventDefault();
                    e.stopPropagation();
                    dropdown.classList.toggle('active');

                    // Close other dropdowns
                    dropdowns.forEach(other => {
                        if (other !== dropdown) other.classList.remove('active');
                    });
                }
            });
        }
    });

    // Close dropdown when clicking outside
    window.addEventListener('click', () => {
        dropdowns.forEach(dropdown => {
            dropdown.classList.remove('active');
        });
    });

    // Mobile Menu Toggle
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const nav = document.querySelector('nav');

    if (mobileMenuBtn && nav) {
        mobileMenuBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            nav.classList.toggle('active');
            // Change icon
            const icon = mobileMenuBtn.querySelector('i');
            if (nav.classList.contains('active')) {
                icon.classList.replace('fa-bars', 'fa-times');
            } else {
                icon.classList.replace('fa-times', 'fa-bars');
            }
        });
    }

    // Enquiry Popup Logic
    const popupOverlay = document.getElementById('enquiryPopup');
    const closePopup = document.getElementById('closePopup');
    const sideBtn = document.getElementById('sideEnquiryBtn');

    if (popupOverlay) {
        // Show popup after 1 second ONLY on the home page
        const isHomePage = window.location.pathname === '/' || window.location.pathname === '/home' || window.location.pathname.endsWith('index.html');
        if (isHomePage) {
            setTimeout(() => {
                popupOverlay.style.display = 'flex';
            }, 1000);
        }

        if (closePopup) {
            closePopup.addEventListener('click', () => {
                popupOverlay.style.display = 'none';
            });
        }

        if (sideBtn) {
            sideBtn.addEventListener('click', () => {
                popupOverlay.style.display = 'flex';
            });
        }

        // Close on clicking overlay background
        popupOverlay.addEventListener('click', (e) => {
            if (e.target === popupOverlay) {
                popupOverlay.style.display = 'none';
            }
        });
    }

    // PDF Download Logic
    const downloadBtn = document.getElementById('download-pdf-btn');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', () => {
            const element = document.getElementById('admission-form');
            const opt = {
                margin: 10,
                filename: 'IPSM_Little_Wonders_Admission_Form.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2 },
                jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
            };

            // New Window style preparation (optional but good for PDF layout)
            downloadBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Generating...';

            html2pdf().set(opt).from(element).save().then(() => {
                downloadBtn.innerHTML = '<i class="fas fa-file-download"></i> Download Form';
            });
        });
    }

    // Gallery Filtering Logic
    const tabBtns = document.querySelectorAll('.tab-btn');
    const galleryItems = document.querySelectorAll('.gallery-item');

    if (tabBtns.length > 0) {
        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                // Remove active class from all buttons
                tabBtns.forEach(b => b.classList.remove('active'));
                // Add active class to clicked button
                btn.classList.add('active');

                const filter = btn.getAttribute('data-filter');

                galleryItems.forEach(item => {
                    if (filter === 'all' || item.getAttribute('data-category') === filter) {
                        item.style.display = 'block';
                        // Add a small animation effect
                        item.style.animation = 'fadeInUp 0.5s ease forwards';
                    } else {
                        item.style.display = 'none';
                    }
                });
            });
        });

        // Video Modal Logic
        const videoModal = document.getElementById('videoModal');
        const videoPlayer = document.getElementById('videoPlayer');
        const closeVideoModal = document.getElementById('closeVideoModal');

        galleryItems.forEach(item => {
            if (item.hasAttribute('data-video')) {
                item.addEventListener('click', () => {
                    const videoUrl = item.getAttribute('data-video');
                    videoPlayer.src = videoUrl;
                    videoModal.style.display = 'flex';
                });
            }
        });

        if (closeVideoModal) {
            closeVideoModal.addEventListener('click', () => {
                videoModal.style.display = 'none';
                videoPlayer.src = '';
            });
        }

        if (videoModal) {
            videoModal.addEventListener('click', (e) => {
                if (e.target === videoModal) {
                    videoModal.style.display = 'none';
                    videoPlayer.src = '';
                }
            });
        }
    }

    // Hero Slider Logic - Seamless Infinite Horizontal Sliding
    const slider = document.querySelector('.hero-slider');
    const slides = document.querySelectorAll('.slide-item');
    if (slider && slides.length > 0) {
        let currentSlide = 0;
        const totalSlides = slides.length - 1; // Not counting the clone
        const slideInterval = 5000; // 5 seconds per slide

        const nextSlide = () => {
            currentSlide++;
            slider.style.transition = 'transform 1.2s cubic-bezier(0.645, 0.045, 0.355, 1)';
            slider.style.transform = `translateX(-${currentSlide * 100}%)`;

            // If we reached the clone, jump back to the first slide instantly
            if (currentSlide === totalSlides) {
                setTimeout(() => {
                    slider.style.transition = 'none';
                    currentSlide = 0;
                    slider.style.transform = `translateX(0)`;
                }, 1200); // Match transition time
            }
        };

        setInterval(nextSlide, slideInterval);
    }
});
