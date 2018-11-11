package email;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.DatatypeConverter;

import teamworks.TWList;
import teamworks.TWObject;

/**********************************************
 * Simple java connector to send email with the following features:
 *  - No Auth
 *  - No SSL/TLS
 * 
 **********************************************/
@SuppressWarnings("restriction")
public class EmailUtility {

    public static void main(String[] args) {
        sendEmailNoAuth("localhost", 25, "timhuynh1301@gmail.com", "thong.huynh@vsource-software.com", "test thong send email", "test thong send email", null);
    }
    
    public static void sendEmailNoAuth(
            String smtpServer, int smtpPort,
            String mailFrom, String mailTo,
            String mailSubject, String mailText,
            TWList ecmContent) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.debug", "false");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            // Message
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipient(RecipientType.TO, new InternetAddress(mailTo));
            message.setSubject(mailSubject);
            message.setSentDate(new Date());
            // Message content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mailText);
            Multipart multipart = new MimeMultipart();
            // Text
            multipart.addBodyPart(messageBodyPart);
            // Attachment
            if (ecmContent != null) {
                for (int i = 0; i < ecmContent.getArraySize(); i++) {
                    TWObject content = (TWObject) ecmContent.getArrayData(i);
                    String currentFile = (String) content.getPropertyValue("content");
                    String name = (String) content.getPropertyValue("fileName");
                    String mimeType = (String) content.getPropertyValue("mimeType");
                    
                    addAttachment(multipart, currentFile, mimeType, name);
                    
                }
            }
            // Test
//            byte[] obj = null;
//            String objMime = "text/plain";
//            String filename = "test";
//            addAttachment(multipart, "dGVzdDI=", objMime, filename);
         
            // Send the complete message parts
            message.setContent(multipart);
            
            Transport.send(message);
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static void addAttachment(Multipart multipart, String currentFile, String mimeType, String filename) {       
        try {
            byte[] tempDocData = DatatypeConverter.parseBase64Binary(currentFile);
            BodyPart messageBodyPart = new MimeBodyPart();      
            DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(tempDocData, mimeType));
            messageBodyPart.setDataHandler(dataHandler);
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
