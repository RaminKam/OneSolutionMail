package com.solmail.services;

import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solmail.exceptions.AppException;
import com.solmail.payload.MailStatus;

@Service
public class ImapService {
	private static final Logger logger = LoggerFactory.getLogger(ImapService.class);

	@Value("${spring.mail.username}")
	private String email_id;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${com.solmail.headername}")
	private String headerName;

	@Value("${com.solmail.imap.host}")
	private String imapHost;

	@Value("${com.solmail.imap.port}")
	private String imapPort;
	
	
	@Value("${com.solmail.mailsubject}")
	private String mailSubject;

	public MailStatus getStatus(String uuid) {
		MailStatus mailStatus = MailStatus.NOT_DELIVERIED;

		Properties properties = new Properties();
		properties.put("mail.store.protocol", "imaps");
		properties.put("mail.imaps.host", imapHost);
		properties.put("mail.imaps.port", imapPort);

		try {

			Session session = Session.getDefaultInstance(properties, null);
			Store store = session.getStore("imaps");
			store.connect(email_id, password);

			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);

			int messageCount = inbox.getMessageCount();

			for (int i = messageCount; i > 0; i--) {
				Message msg = inbox.getMessage(i);

				if (msg.getSubject().equals(mailSubject)) {
					Enumeration<Header> headers = msg.getAllHeaders();

					while (headers.hasMoreElements()) {
						Header header = headers.nextElement();
						String name = header.getName();
						String value = header.getValue();

						if (name.equals(headerName)) {

							if (value.equals(uuid)) {
							
								mailStatus = MailStatus.DELIVERIED;

							} 
						}
	
					}

				}


			}


			inbox.close(true);
			store.close();

		} catch (Exception ex) {
			AppException.error("Checking status error", ex);
		}
		
		logger.info("Mail status with uuid="+uuid+" "+mailStatus);

		return mailStatus;

	}
}
