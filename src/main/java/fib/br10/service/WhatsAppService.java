package fib.br10.service;

import fib.br10.service.abstracts.SmsService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Log4j2
public class WhatsAppService implements SmsService {

    private static final String API_URL = "https://graph.facebook.com/v13.0/<PHONE_NUMBER_ID>/messages";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";

    public void sendMessage(String recipientPhoneNumber, String messageText) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        String.format("""
                                        {
                                        messaging_product : whatsapp,
                                        "to": "%s",
                                        "type": "text",
                                        "text": {"body": "%s"}
                                        }
                                        """,
                                recipientPhoneNumber, messageText)))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(log::info)
                .exceptionally(e -> {
                    log.error("Error sending message: ", e);
                    return null;
                });
    }
}
