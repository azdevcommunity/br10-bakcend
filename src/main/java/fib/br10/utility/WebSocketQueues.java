package fib.br10.utility;

public final class WebSocketQueues {

    private WebSocketQueues() {
    }

    public static final String RESERVATION_CREATED = "/topic/reservations/created";

    public static final String RESERVATION_UPDATED = "/topic/reservations/updated";

    public static final String RESERVATION_CANCELED = "/topic/reservations/canceled";

}
