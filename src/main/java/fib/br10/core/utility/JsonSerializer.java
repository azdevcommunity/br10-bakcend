package fib.br10.core.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fib.br10.core.exception.BaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class JsonSerializer {

    ObjectMapper objectMapper;

    public <T> T deserialize(String json, Class<T> clazz) {
        return handleException(() -> objectMapper.readValue(json, clazz));
    }

    public String serialize(Object object) {
        return handleException(() -> objectMapper.writeValueAsString(object));
    }

    private <T> T handleException(JsonAction<T> action ) {
        try {
            return action.apply();
        } catch (JsonProcessingException e) {
            log.error("JsonSerializer exception", e);
            throw new BaseException(e);
        }
    }
    @FunctionalInterface
    private interface JsonAction<T> {
        T apply() throws JsonProcessingException;
    }
}