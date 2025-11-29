package com.portfolio.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    @Value("${spring.mail.password}")
    private String sendGridApiKey;
    
    @Value("${app.admin-email}")
    private String adminEmail;
    
    @Value("${app.frontend-url}")
    private String frontendUrl;
    
    public void sendProjectRequestConfirmation(String toEmail, String clientName, Long requestId) {
        String subject = "Project Request Received - Confirmation";
        String content = buildRequestConfirmationEmail(clientName, requestId);
        sendEmail(toEmail, subject, content);
    }
    
    public void sendAdminNewRequestAlert(String clientName, String projectTitle, Long requestId) {
        String subject = "🔔 New Project Request: " + projectTitle;
        String content = buildAdminAlertEmail(clientName, projectTitle, requestId);
        sendEmail(adminEmail, subject, content);
    }
    
    public void sendStatusUpdateEmail(String toEmail, String clientName, String newStatus, Long requestId) {
        String subject = "Project Request Status Update";
        String content = buildStatusUpdateEmail(clientName, newStatus, requestId);
        sendEmail(toEmail, subject, content);
    }
    
    public void sendResponseNotification(String toEmail, String clientName, String message, Long requestId) {
        String subject = "New Response to Your Project Request";
        String content = buildResponseNotificationEmail(clientName, message, requestId);
        sendEmail(toEmail, subject, content);
    }
    
    public void sendContactMessageNotification(String name, String email, String message) {
        String subject = "📨 New Contact Form Submission";
        String content = buildContactNotificationEmail(name, email, message);
        sendEmail(adminEmail, subject, content);
    }
    
    private void sendEmail(String to, String subject, String htmlContent) {
        Email from = new Email(adminEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);
        
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Email sent successfully. Status: {}", response.getStatusCode());
        } catch (IOException ex) {
            log.error("Error sending email", ex);
        }
    }
    
    private String buildRequestConfirmationEmail(String clientName, Long requestId) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Thank You for Your Project Request!</h2>
                <p>Hi %s,</p>
                <p>I've received your project request and will review it shortly.</p>
                <p><strong>Request ID:</strong> #%d</p>
                <p>I typically respond within 24-48 hours. You'll receive an email once I've reviewed your request.</p>
                <p>Best regards,<br>Your Portfolio Team</p>
            </body>
            </html>
            """, clientName, requestId);
    }
    
    private String buildAdminAlertEmail(String clientName, String projectTitle, Long requestId) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>New Project Request</h2>
                <p><strong>Client:</strong> %s</p>
                <p><strong>Project:</strong> %s</p>
                <p><strong>Request ID:</strong> #%d</p>
                <p><a href="%s/admin/requests/%d" style="background: #6366F1; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">View Request</a></p>
            </body>
            </html>
            """, clientName, projectTitle, requestId, frontendUrl, requestId);
    }
    
    private String buildStatusUpdateEmail(String clientName, String newStatus, Long requestId) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>Project Request Status Updated</h2>
                <p>Hi %s,</p>
                <p>Your project request (ID: #%d) status has been updated to: <strong>%s</strong></p>
                <p>I'll be in touch with more details soon.</p>
                <p>Best regards,<br>Your Portfolio Team</p>
            </body>
            </html>
            """, clientName, requestId, newStatus);
    }
    
    private String buildResponseNotificationEmail(String clientName, String message, Long requestId) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>New Response to Your Request</h2>
                <p>Hi %s,</p>
                <p>I've responded to your project request (ID: #%d):</p>
                <blockquote style="border-left: 4px solid #6366F1; padding-left: 15px; color: #555;">%s</blockquote>
                <p>Best regards,<br>Your Portfolio Team</p>
            </body>
            </html>
            """, clientName, requestId, message);
    }
    
    private String buildContactNotificationEmail(String name, String email, String message) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2>New Contact Form Message</h2>
                <p><strong>From:</strong> %s (%s)</p>
                <p><strong>Message:</strong></p>
                <p>%s</p>
            </body>
            </html>
            """, name, email, message);
    }
}