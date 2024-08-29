package fib.br10.core.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {


    public boolean isDevelopment() {
        return Objects.equals(System.getenv("BR10_PROFILE"), "dev");
    }


    public boolean isProd() {
        return Objects.equals(System.getenv("BR10_PROFILE"), "prod");
    }

}
