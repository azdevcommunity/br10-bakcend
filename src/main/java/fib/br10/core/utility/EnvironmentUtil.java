package fib.br10.core.utility;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {

    private final Environment environment;

    public boolean isDevelopment() {
        String[] activeProfiles =  environment.getActiveProfiles();
        return Arrays.stream(activeProfiles)
                .anyMatch(x->x.equals("dev") || x.equals("docker"));
    }
}
