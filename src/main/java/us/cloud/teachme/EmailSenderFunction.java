package us.cloud.teachme;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import us.cloud.teachme.sender.SimpleEmailSender;

import java.util.Optional;

public class EmailSenderFunction {

    private final SimpleEmailSender mailSender;

    public EmailSenderFunction() {
        this.mailSender = new SimpleEmailSender();
    }

    /**
     * This function listens at endpoint "/api/sendMail". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/sendMail
     * 2. curl "{your host}/api/sendMail?name=HTTP%20Query"
     */
    @FunctionName("sendMail")
    public HttpResponseMessage sendMail(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<EmailRequest>> request,
            final ExecutionContext context) {
        Optional<EmailRequest> emailRequest = request.getBody();
        if (emailRequest.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Invalid email request").build();
        }

        try {
            mailSender.send(
                    emailRequest.get().getTo(),
                    emailRequest.get().getSubject(),
                    emailRequest.get().getBody()
            );

            return request.createResponseBuilder(HttpStatus.OK)
                    .body("Email sent successfully").build();
        } catch (Exception e) {
            context.getLogger().severe("Error sending email: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email").build();
        }
    }
}
