package com.littlewonders.security;

import com.littlewonders.model.SecurityAudit;
import com.littlewonders.repository.SecurityAuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Component
public class SecurityEventListener {

    @Autowired
    private SecurityAuditRepository securityAuditRepository;

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        logEvent(username, "FAILED", "Invalid password attempt");
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        logEvent(username, "SUCCESS", "User logged in");
    }

    private void logEvent(String username, String status, String details) {
        SecurityAudit audit = new SecurityAudit();
        audit.setUsername(username);
        audit.setStatus(status);
        audit.setDetails(details);
        
        // Get IP Address
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getRemoteAddr();
        audit.setIpAddress(ip);
        
        securityAuditRepository.save(audit);
    }
}
