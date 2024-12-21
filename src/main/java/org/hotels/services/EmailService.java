package org.hotels.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    public boolean send(String subject, String body, String emailToAddress) {
        try {
            System.out.println("Before send email");
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            String companyEmail = System.getenv("COMPANY_EMAIL");
            System.out.println(">>>>>> companyEmail " + companyEmail);
            String companyEmailPassword = System.getenv("COMPANY_EMAIL_PASSWORD");
            System.out.println(">>>>>> companyEmailPassword " + companyEmailPassword);
            System.out.println(companyEmail);
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

//            System.out.println("Start to send email");
//            Transport.send(message);
//            System.out.println("Email sent successfully");
            return true;
        } catch (Exception exception) {
            System.err.println("Error sending email " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        String adsd = "Aloft Harlem";
        System.out.println(adsd.replace(" ", "-"));
    }
}
