package com.trimq.notification.service;

import com.trimq.notification.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service for sending emails using Spring Mail.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailConfig emailConfig;

    /**
     * Send a simple text email.
     * 
     * @param to Recipient email
     * @param subject Email subject
     * @param text Email body
     * @return true if sent successfully
     */
    public boolean sendSimpleEmail(String to, String subject, String text) {
        if (!emailConfig.isEnabled()) {
            log.info("Email disabled. Would send to: {}, subject: {}", to, subject);
            return true;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailConfig.getFrom(), emailConfig.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            
            mailSender.send(message);
            log.info("Simple email sent to: {}", to);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            return false;
        }
    }

    /**
     * Send an HTML email using a template.
     * 
     * @param to Recipient email
     * @param subject Email subject
     * @param templateName Thymeleaf template name
     * @param variables Template variables
     * @return true if sent successfully
     */
    public boolean sendTemplateEmail(String to, String subject, String templateName, 
                                      Map<String, Object> variables) {
        if (!emailConfig.isEnabled()) {
            log.info("Email disabled. Would send template {} to: {}", templateName, to);
            return true;
        }

        try {
            // Process template
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);
            
            // Send email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailConfig.getFrom(), emailConfig.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Template email sent to: {}, template: {}", to, templateName);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to send template email to: {}", to, e);
            return false;
        }
    }

    /**
     * Send email asynchronously.
     */
    @Async("notificationExecutor")
    public CompletableFuture<Boolean> sendTemplateEmailAsync(String to, String subject, 
                                                              String templateName, 
                                                              Map<String, Object> variables) {
        boolean result = sendTemplateEmail(to, subject, templateName, variables);
        return CompletableFuture.completedFuture(result);
    }
}

