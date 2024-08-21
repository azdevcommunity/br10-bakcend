package fib.br10.service.abstracts;


public interface WebSocketHandler {

    <T> void publish(String queueName, T payload);

    <T> void publish(String queueName, T payload, String user);

}
