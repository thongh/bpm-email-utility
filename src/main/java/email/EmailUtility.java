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
    private static final String MAIL_HOST_IP = "192.168.101.60";
    private static final String MAIL_PORT = "25";
    
    public static void main(String[] args) {
        sendEmail("", "", "", "");
    }

    public static void sendEmail(String filePath, String fileName,
            String mailFrom, String mailTo) {

        if (filePath == "") {
            filePath = "/tmp/files/test.csv";
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

        // Setup mail server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", MAIL_HOST_IP);
        props.put("mail.smtp.port", MAIL_PORT);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(props);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(mailFrom));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mailTo));

            // Set Subject: header field
            message.setSubject("Email proforma invoices to Franchises");
        
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            
            // Fill the message
            messageBodyPart.setText("Email proforma invoices to Franchises");
            
            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            
            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filePath);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
