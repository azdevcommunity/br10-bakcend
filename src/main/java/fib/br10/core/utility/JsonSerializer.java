package fib.br10.core.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fib.br10.core.exception.BaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.management.ObjectName;
import java.nio.charset.StandardCharsets;


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

    public JsonNode toJsonNode(String object) {
        return handleException(() -> objectMapper.readTree(object));
    }

    private <T> T handleException(JsonAction<T> action ) {
        try {
            return action.apply();
        } catch (Exception e) {
            log.error("JsonSerializer exception", e);
            throw new BaseException(e);
        }
    }
    @FunctionalInterface
    private interface JsonAction<T> {
        T apply() throws Exception;
    }
}