package email;

import javax.mail.Message.RecipientType;
import javax.xml.bind.DatatypeConverter;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

import teamworks.TWList;
import teamworks.TWObject;

/********************************************************
 * Java connector to send emails in IBM BPM
 * 
 * @author Thong Huynh
 * ******************************************************
 */
public class EmailUtility {

    public static void main(String[] args) {
        // Test here
    }
    

    public static void sendEmail(
            String smtpHost, int smtpPort, String login, String password,
            String mailFromName, String mailFromAddr, 
            String mailToName, String mailToAddr, 
            String mailSubject, String mailText,
            TWList ecmContent,
            boolean ssl, boolean tls) {
        Email email = new Email();
        email.setSubject(mailSubject);
        if (mailText != null && !mailText.isEmpty()) {
            email.setText(mailText);
        }
        email.setFromAddress(mailFromName, mailFromAddr);
        email.addRecipient(mailToName, mailToAddr, RecipientType.TO);
        if (ecmContent != null) {
            attachECMContentStream(email, ecmContent);
        }
        TransportStrategy transport = TransportStrategy.SMTP_PLAIN;
        if (ssl) {
            transport = TransportStrategy.SMTP_SSL;
        } else if (tls) {
            transport = TransportStrategy.SMTP_TLS;
        }
        new Mailer(smtpHost, smtpPort, login, password, transport).sendMail(email);

    }
    
    private static void attachECMContentStream(Email email, TWList ecmContent) {
        if (ecmContent != null) {
            for (int i = 0; i < ecmContent.getArraySize(); i++) {
                TWObject content = (TWObject) ecmContent.getArrayData(i);
                String currentFile = (String) content.getPropertyValue("content");
                byte[] tempDocData = DatatypeConverter.parseBase64Binary(currentFile);
                String name = (String) content.getPropertyValue("fileName");
                String mimeType = (String) content.getPropertyValue("mimeType");
                email.addAttachment(name, tempDocData, mimeType);
            }
        }
    }
}
