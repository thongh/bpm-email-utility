package email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtility {

	private static final String MAIL_FROM = "support@econet.co.zw";
	private static final String MAIL_TO = "daisy.dzingiso@econet.co.zw";
	private static final String MAIL_HOST = "mail.econet.co.zw";

//	private static final String FILE_PATH = "/staging/cvbs_files";
//	private static final String FILE_HOST = "192.168.81.126";
//	private static final String FILE_USER = "bpm";

	public static void sendEmail(String filePath, String fileName, 
			String mailFrom, String mailTo) {
		
		if (filePath == "") {
			filePath = "/staging/cvbs_files/EWZ_ISM_July2018_Bill_Analysis.xlsx";
		}
		
		if (fileName == "") {
			fileName = "test";
		}
		
		if (mailFrom == "") {
			mailFrom = MAIL_FROM;
		}
		
		if (mailTo == "") {
			mailTo = MAIL_TO;
		}

		// Get system properties
//		Properties properties = System.getProperties();

		// Setup mail server
		Properties props = new Properties();
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", MAIL_HOST);
		props.put("mail.smtp.port", "25");
		Session session = Session.getDefaultInstance(props);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(mailFrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));

			// Set Subject: header field
			message.setSubject("Email proforma invoices to Franchises");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("Email proforma invoices to Franchises");

			// Create a multipart message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			System.out.println("Sending email....");
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
