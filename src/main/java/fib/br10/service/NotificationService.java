package fib.br10.service;

import com.google.firebase.messaging.*;
import fib.br10.dto.notification.PushNotificationRequest;
import fib.br10.entity.Notification;
import fib.br10.exception.notification.NotificationException;
import fib.br10.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@AllArgsConstructor
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenService fcmTokenService;
    private final NotificationRepository notificationRepository;

    @Async
    public void sendAll(List<PushNotificationRequest> requests) {
        requests.forEach(this::send);
    }

    @Async
    @Deprecated
    public void sendAll(List<String> tokens, String body, String title) {
        validateInputs(body, title, tokens);

        MulticastMessage message = getMulticastMessage(tokens, body, title);

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            handleMulticastMessageResponse(response.getResponses(), tokens);
        } catch (FirebaseMessagingException e) {
            log.error("Error sending multicast message, Error code: {}", e.getErrorCode(), e);
            throw new NotificationException();
        }
    }

    @Async
    public void send(PushNotificationRequest request) {
        validateRequest(request);

        Message message = getMessage(request);

        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent response: {}", response);
            log.info("Successfully sent message to token: {}", request.getTargetToken());
            saveNotification(request);
        } catch (FirebaseMessagingException e) {
            handleSendException(e, request.getTargetToken());
            throw new NotificationException();
        }
    }


    private void saveNotification(PushNotificationRequest request) {
        notificationRepository.save(Notification.builder()
                .body(request.getBody())
                .title(request.getTitle())
                .userId(request.getUserId())
                .build());
    }

    private void handleMulticastMessageResponse(List<SendResponse> responses, List<String> tokens) {
        for (int i = 0; i < responses.size(); i++) {
            SendResponse response = responses.get(i);
            if (!response.isSuccessful()) {
                handleSendException(response.getException(), tokens.get(i));
            }
        }
    }

    private void handleSendException(FirebaseMessagingException e, String token) {
        if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
            fcmTokenService.delete(token);
            log.warn("Token is unregistered or expired, removing from database: {}", token);
        } else {
            log.error("Error sending notification to token: {}, Error code: {}", token, e.getErrorCode(), e);
        }
    }

    private void validateRequest(PushNotificationRequest request) {
        if (Objects.isNull(request.getTargetToken()) || Objects.isNull(request.getTitle()) || Objects.isNull(request.getBody())) {
            log.error("Missing required fields in PushNotificationRequest");
            throw new NotificationException();
        }
    }

    private void validateInputs(String body, String title, List<String> tokens) {
        if (Objects.isNull(body) || Objects.isNull(title) || tokens == null || tokens.isEmpty()) {
            log.error("Missing required fields");
            throw new NotificationException();
        }
    }

    private Message getMessage(PushNotificationRequest request) {
        return Message.builder()
                .setToken(request.getTargetToken())
                .putData("title", request.getTitle())
                .putData("body", request.getBody())
                .build();
    }

    private MulticastMessage getMulticastMessage(List<String> tokens, String title, String body) {
        return MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(tokens)
                .build();
    }
}
