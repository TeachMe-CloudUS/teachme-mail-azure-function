# Azure Function for Sending Emails

This repository contains an Azure Function implemented in Java that provides a basic email-sending service. The function is triggered via an HTTP POST request and is designed to be simple and modular, making it suitable for integration into cloud-based systems.

---

## Features

- **HTTP-triggered Email Sending:**
    - Accepts an HTTP POST request to send an email with configurable fields (`to`, `subject`, `body`).
- **Modular Design:**
    - The functionality is implemented using a dedicated email-sending class, `SimpleEmailSender`, to keep the codebase clean and modular.
- **Error Handling:**
    - Provides appropriate HTTP status codes and error messages for malformed requests or runtime errors.

---

## How It Works

1. **Trigger:**
    - The Azure Function is triggered using an HTTP POST request. It is configured to allow anonymous access (`AuthorizationLevel.ANONYMOUS`).

2. **Input Format:**
    - The request body must contain a JSON object with the following fields:
      ```json
      {
          "to": "email@example.com",
          "subject": "Subject line",
          "body": "Email content here"
      }
      ```

3. **Processing:**
    - The function validates the input. If the input is invalid, a `400 Bad Request` response is returned.
    - If valid, the function hands email delivery to `SimpleEmailSender` and responds either with:
        - **200 OK** if successful
        - **400 OK** if invalid body, or
        - **500 Internal Server Error** in case of a failure.

---

## Prerequisites

1. **Azure Subscription:**
    - An active Azure subscription is required to deploy the function.

2. **Development Tools:**
    - Java Development Kit (JDK) 11 or higher.
    - [Maven](https://maven.apache.org/) for building the project.
    - [Azure Functions Core Tools](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local) for testing and deployment.

3. **Configuration for Email-Sending:**
    - The implementation depends on `SimpleEmailSender` for sending emails. This class should be configured to use an appropriate email delivery provider (e.g., an SMTP server or an external API such as SendGrid).

---

## Usage

### Sending an Email

- Example of an HTTP POST request (using `curl`):
  ```bash
  curl -X POST https://<YOUR_FUNCTION_APP_NAME>.azurewebsites.net/api/sendMail \
  -H "Content-Type: application/json" \
  -d '{
        "to": "recipient@example.com",
        "subject": "Test Email",
        "body": "This is an example email."
      }'
  ```

### Possible Responses:
- **200 OK:** Email was successfully sent.
- **400 Bad Request:** The input JSON was invalid or required fields were missing.
- **500 Internal Server Error:** Failed to send the email due to an error (e.g., an issue with the email service).

---

## Deployment

1. **Build the Project:**
    - To build the project, run:
      ```bash
      mvn clean package
      ```

2. **Deploy to Azure:**
    - Use the Azure Functions Core Tools to deploy:
      ```bash
      func azure functionapp publish <YOUR_FUNCTION_APP_NAME>
      ```

3. **Test the Function:**
    - After successful deployment, the function will be available at:
      ```
      https://<YOUR_FUNCTION_APP_NAME>.azurewebsites.net/api/sendMail
      ```

---

## Running Locally

1. Install the prerequisites mentioned above.
2. Start the Azure Functions host:
   ```bash
   mvn azure-functions:run
   ```
3. Test the function locally using POST requests to:
   ```
   http://localhost:7071/api/sendMail
   ```

---

## Note on `SimpleEmailSender`

> Ensure that the `SimpleEmailSender` is correctly configured to connect to your SMTP server. Credentials can be set in a `local.settings.json` or in any `.env`-file.