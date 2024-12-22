package org.hotels.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public boolean send(String subject, String body, String emailToAddress) {
        try {
            logger.info("Before send email");
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            String companyEmail = System.getenv("COMPANY_EMAIL");
            String companyEmailPassword = System.getenv("COMPANY_EMAIL_PASSWORD");
            Session session = Session.getInstance(prop,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(companyEmail, companyEmailPassword);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(companyEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailToAddress)
            );
            message.setSubject(subject);
            message.setText(body);

            logger.info("Sending the email");
            Transport.send(message);
            logger.info("Email sent successfully");
            return true;
        } catch (Exception exception) {
            logger.error("Error while sending the email ", exception);
            return false;
        }
    }
}
