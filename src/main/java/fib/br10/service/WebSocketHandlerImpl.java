package fib.br10.service;

import fib.br10.exception.user.UserIdRequiredException;
import fib.br10.exception.websocket.WebSocketPublishException;
import fib.br10.exception.websocket.WebSocketQueueNameCannotBeNullException;
import fib.br10.exception.websocket.WebSocketRequestCannotBeNullException;
import fib.br10.service.abstracts.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Log4j2
public class WebSocketHandlerImpl implements WebSocketHandler {
    
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public <T> void publish(String queueName, T payload) {
        publish(() -> {
            validateRequest(queueName, payload);
            messagingTemplate.convertAndSend(queueName, payload);
        });
    }

    @Override
    public <T> void publish(String queueName, T payload, String user) {
        publish(() -> {
            validateRequest(queueName, payload, user);
            messagingTemplate.convertAndSendToUser(user, queueName, payload);
        });
    }

    private <T> void validateRequest(String queueName, T payload, String user) {
        if (Objects.isNull(queueName)) {
            log.error("queueName is null");
            throw new WebSocketQueueNameCannotBeNullException();
        }
        if (Objects.isNull(payload)) {
            log.error("queueName: {} socket payload is null", queueName);
            throw new WebSocketRequestCannotBeNullException();
        }
        if (Objects.isNull(user)) {
            log.error("queueName :{} socket user is null", queueName);
            throw new UserIdRequiredException();
        }
    }

    private <T> void validateRequest(String queueName, T payload) {
        validateRequest(queueName, payload, "");
    }

    private void publish(PublishAction function) {
        try {
            function.publish();
        } catch (Exception e) {
            log.error(e);
            throw new WebSocketPublishException();
        }
    }

    @FunctionalInterface
    public interface PublishAction {
        void publish() throws Exception;
    }
}