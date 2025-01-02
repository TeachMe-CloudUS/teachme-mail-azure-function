package us.cloud.teachme.sender;

public interface EmailSender {

    void send(String to, String subject, String body) throws Exception;
}
