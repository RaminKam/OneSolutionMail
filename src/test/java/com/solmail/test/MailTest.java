package com.solmail.test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.solmail.controllers.MailController;
import com.solmail.payload.MailStatus;
import com.solmail.services.ImapService;
import com.solmail.services.SmtpService;

@RunWith(MockitoJUnitRunner.class)
public class MailTest {

	@Mock
	private SmtpService smtpService;
	
	@Mock
	private ImapService imapService;
	
	@InjectMocks
	MailController mc;
	
	

	
	@Test
	public void testDeliveried() throws Exception{
		String uuid="123";
		when(smtpService.sendMail()).thenReturn(uuid);
		when(imapService.getStatus(uuid)).thenReturn(MailStatus.DELIVERIED);
		String receivedUuid = mc.send().getBody().getUuid();
		
		assertEquals(uuid,receivedUuid);
		
		MailStatus mailStatusReceived = mc.status(uuid).getBody().getMailStatus();
		
		assertEquals(MailStatus.DELIVERIED,mailStatusReceived);
		
	

		
	}
	
	@Test
	public void testNotdeliveried() throws Exception{
		String uuid="567";
		when(smtpService.sendMail()).thenReturn(uuid);
		when(imapService.getStatus(uuid)).thenReturn(MailStatus.NOT_DELIVERIED);
		String receivedUuid = mc.send().getBody().getUuid();
		
		assertEquals(uuid,receivedUuid);
		
		MailStatus mailStatusReceived = mc.status(uuid).getBody().getMailStatus();
		
		assertEquals(MailStatus.NOT_DELIVERIED,mailStatusReceived);
		
	

		
	}
}
