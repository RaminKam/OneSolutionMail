package com.solmail.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.solmail.payload.CheckStatusResponse;
import com.solmail.payload.MailStatus;
import com.solmail.payload.SendMailResponse;
import com.solmail.services.ImapService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.solmail.Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(ItegrationTest.class);
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Value("${server.port}")
	private String port;
	
	
	@Test
	public void integrationTest() throws Exception {
		ResponseEntity<SendMailResponse> responseEntity = 
				restTemplate.exchange(
						"http://localhost:"+port+"/notification/send"
						, 
						HttpMethod.POST,
						null,
						new ParameterizedTypeReference<SendMailResponse>() {}
						);
		SendMailResponse resp = responseEntity.getBody();
		String receivedUuid = resp.getUuid();
		
		ResponseEntity<CheckStatusResponse> responseEntityStatus = 
				restTemplate.exchange(
						"http://localhost:"+port+"/notification/status/"+receivedUuid
						, 
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<CheckStatusResponse>() {}
						);
		MailStatus mailStatusReceived = responseEntityStatus.getBody().getMailStatus();
		
		logger.info("uuid="+receivedUuid+" status="+mailStatusReceived);
		
		assertEquals(MailStatus.DELIVERIED,mailStatusReceived);
		
		
		
		
		
	}

}
