package fib.br10.configuration;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fib.br10.utility.CloudinaryKeys.CLOUD_NAME;
import static fib.br10.utility.CloudinaryKeys.API_KEY;
import static fib.br10.utility.CloudinaryKeys.API_SECRET;

@Configuration
public class CloudinaryConfig{

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME, cloudName,
                API_KEY, apiKey,
                API_SECRET, apiSecret));
    }
}
