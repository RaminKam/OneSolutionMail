package com.solmail.controllers;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solmail.exceptions.AppException;
import com.solmail.payload.CheckStatusResponse;
import com.solmail.payload.MailStatus;
import com.solmail.payload.SendMailResponse;
import com.solmail.services.ImapService;
import com.solmail.services.SmtpService;


@RestController
@RequestMapping("/notification")
public class MailController {
	@Autowired
	private SmtpService mailService;
	
	@Autowired
	private ImapService imapService;
	
  @PostMapping("/send")
  public ResponseEntity<SendMailResponse> send() {
	  
	  String uuidSended = mailService.sendMail();
	  SendMailResponse sendMailResponse = new SendMailResponse();
	  sendMailResponse.setUuid(uuidSended);
	  
      return new ResponseEntity<SendMailResponse>(sendMailResponse, HttpStatus.OK);
  }
  
  @GetMapping("/status/{uuid}")
  public ResponseEntity<CheckStatusResponse> status(@PathVariable String uuid) {
	  CheckStatusResponse checkStatusResponse = new CheckStatusResponse();
	  
	  MailStatus mailStatus=MailStatus.NOT_DELIVERIED;
	  mailStatus = imapService.getStatus(uuid);
	  
	  checkStatusResponse.setMailStatus(mailStatus);

      return new ResponseEntity<CheckStatusResponse>(checkStatusResponse, HttpStatus.OK);
  }


}