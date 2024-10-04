package fib.br10.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.notification.PushNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@AllArgsConstructor
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendAll(List<PushNotificationRequest> requests) {
        requests.forEach(this::send);
    }

    public void sendAll(List<String> tokens, String body, String title) {
        if (Objects.isNull(body) || Objects.isNull(title)) {
            throw new BaseException("Missing required fields");
        }
        MulticastMessage message = getMulticastMessage(tokens, body, title);
        CompletableFuture.runAsync(() -> {
            try {
                BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
                log.info(response.getResponses());
            } catch (FirebaseMessagingException e) {
                log.error("Error sending multicast message, Error code: {}", e.getErrorCode());
                throw new BaseException(e);
            }
        }).exceptionally(ex -> {
            log.error("Exception during notification sending: ", ex);
            return null;
        });
    }


    public void send(PushNotificationRequest request) {
        Message message = getMessage(request);
        CompletableFuture.runAsync(() -> {
            try {
                String response = firebaseMessaging.send(message);
                log.info("Successfully sent message to token: {}", request.getTargetToken());
            } catch (FirebaseMessagingException e) {
                log.error("Error sending notification to token: {}, Error code: {}", request.getTargetToken(), e.getErrorCode());
                throw new BaseException(e);
            }
        }).exceptionally(ex -> {
            log.error("Exception during notification sending: ", ex);
            return null;
        });
    }


    private Message getMessage(PushNotificationRequest request) {
        if (Objects.isNull(request.getTargetToken()) || Objects.isNull(request.getTitle()) || Objects.isNull(request.getBody())) {
            throw new BaseException("Missing required fields");
        }
        return Message.builder()
                .setToken(request.getTargetToken())
                .putData("title", request.getTitle())
                .putData("body", request.getBody())
                .build();
    }

    private MulticastMessage getMulticastMessage(List<String> tokens, String title, String body) {
        if (Objects.isNull(tokens) || Objects.isNull(title) || Objects.isNull(body)) {
            throw new BaseException("Missing required fields");
        }

        return MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(tokens)
                .build();
    }
}
