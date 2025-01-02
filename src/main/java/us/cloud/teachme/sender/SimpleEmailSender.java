package us.cloud.teachme.sender;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class SimpleEmailSender implements EmailSender {

    private final String smtpHost;
    private final String smtpPort;
    private final String username;
    private final String password;

    public SimpleEmailSender() {
        this.smtpHost = System.getenv("SMTP_HOST");
        this.smtpPort = System.getenv("SMTP_PORT");
        this.username = System.getenv("SMTP_USERNAME");
        this.password = System.getenv("SMTP_PASSWORD");
    }

    public void send(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body, "text/html");

        Transport.send(message);
    }
}
