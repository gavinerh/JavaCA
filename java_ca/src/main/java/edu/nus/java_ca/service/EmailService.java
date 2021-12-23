package edu.nus.java_ca.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;


@SpringBootApplication
public class EmailService {

	@Autowired
    private JavaMailSender javaMailSender;
    
    public void sendEmailApprove(Leave leave) {

        SimpleMailMessage msg = new SimpleMailMessage();
        String urlString = "http://localhost:8080/login";
        String subject = "Your " +leave.getType() + " leave is approved.";
        String content = "Your " +leave.getType() + " leave from" + leave.getStartDate().toString() + " to " + leave.getEndDate().toString() + " is approved.\n" +urlString;
       	
        msg.setTo(leave.getUser().getEmail());
        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);

    }
    
    public void sendEmailReject(Leave leave) {

        SimpleMailMessage msg = new SimpleMailMessage();
        String urlString = "http://localhost:8080/login";
        String subject = "Your " +leave.getType() + " leave is rejected.";
        String content = "Your " +leave.getType() + " leave from" + leave.getStartDate().toString() + " to " + leave.getEndDate().toString() + " is rejected.\n" + urlString;
        		
        msg.setTo(leave.getUser().getEmail());
        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);

    }
    public void sendEmailApply(Leave leave) {
        User user = leave.getUser();
        SimpleMailMessage msg = new SimpleMailMessage();
        String urlString = "http://localhost:8080/login";
        String subject = "Application of" +leave.getType() + " leave from "+user.getFirstName()+" "+user.getLastName();
        String content = "Application of" +leave.getType() + " leave from "+user.getFirstName()+" "+user.getLastName() + "," + leave.getStartDate().toString() + " to " + leave.getEndDate().toString() + " is pending.\n" + urlString;
        		
        msg.setTo(user.getApprovingOfficer().getEmail());
        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);

    }
    
    
    

    void sendEmailWithAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("e0838414@u.nus.edu");

        helper.setSubject("Testing from Spring Boot");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Check attachment for image!</h1>", true);

        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(msg);

    }
}
