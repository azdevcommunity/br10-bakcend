package fib.br10.controller;

import fib.br10.dto.messages.TodoItem;
import fib.br10.service.abstracts.WebSocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebSocketController {
    WebSocketHandler webSocketHandler;
    private static final ArrayList<TodoItem> todoItems = new ArrayList<>();

    @MessageMapping("/todo/add")
    public void addTodoItem(@Payload TodoItem todoItem, Principal principal) {
        todoItems.add(todoItem);
        webSocketHandler.publish("/topic/all-todos",todoItem);
    }
}
