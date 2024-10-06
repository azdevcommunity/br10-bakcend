package fib.br10.service.abstracts;

public interface SmsService {

    void sendMessage(String recipientPhoneNumber, String messageText);
}
