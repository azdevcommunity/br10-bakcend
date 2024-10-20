package fib.br10.service.abstracts;

import fib.br10.dto.notification.PushNotificationRequest;

import java.util.List;

public interface NotificationService {

     void sendAll(List<PushNotificationRequest> requests);

     void sendAll(List<String> tokens, String body, String title) ;

     void send(PushNotificationRequest request) ;

}
