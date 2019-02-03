package com.solmail.services;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.solmail.exceptions.AppException;
@Service
public class SmtpService {
	private static final Logger logger = LoggerFactory.getLogger(SmtpService.class);
	
    @Autowired
    public JavaMailSender emailSender;	

    
    @Value("${spring.mail.username}")
    private String emailTo;
    
    @Value("${spring.mail.username}")
    private String emailFrom;
    
    @Value("${com.solmail.headername}")
    private String headerName;
    
	@Value("${com.solmail.mailsubject}")
	private String mailSubject;
	
    public String sendMail() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
    	try {
    		
			MimeMessage mm = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mm, true);
			mm.setHeader(headerName, generatedString);
			helper.setTo(emailTo);
			helper.setFrom(emailFrom);
			helper.setSubject(mailSubject);
			helper.setText("Post with uuid:" + generatedString);
			emailSender.send(mm);
        
        
        }
    	catch(Exception ex) 
    	{
        	AppException.error("Sending email error",ex);
        }
    	
    	logger.info("Mail with uuid="+generatedString+" was sended");
    	
    	return generatedString;
    	
    }
}
