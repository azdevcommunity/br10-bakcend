package fib.br10.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.0")
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0")
                        .description("Sample API documentation using Swagger"));
//                        .contact(new Contact().name("Your Name").email("your-email@example.com")))
    }
}