package fib.br10.core.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Configuration
public class LocalizationConfiguration {

    @Bean
    public MessageSource messageSource() {
        YamlMessageSource messageSource = new YamlMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static class YamlMessageSource extends ReloadableResourceBundleMessageSource {

        private final Yaml yaml = new Yaml();
        private final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

        @Override
        protected Properties loadProperties(Resource resource, String filename) throws IOException {
            Properties properties = new Properties();
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                Map<String, Object> yamlMap = yaml.load(reader);
                flattenMap(properties, "", yamlMap);
            } catch (Exception e) {
                logger.error(e);
            }
            return properties;
        }

        @Override
        protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
            if (filename == null) {
                return new PropertiesHolder();
            }

            try {
                Resource[] resources = resourceResolver.getResources(filename + "*.yml");
                Properties mergedProps = new Properties();

                for (Resource resource : resources) {
                    Properties props = loadProperties(resource, filename);
                    mergedProps.putAll(props);
                }
                return new PropertiesHolder(mergedProps, 0);
            } catch (IOException ex) {
                logger.warn("Could not parse YAML file [" + filename + "]", ex);
                return null;
            }
        }

        //     @SuppressWarnings("unchecked")
        private void flattenMap(Properties properties, String path, Map<String, Object> source) {
            source.forEach((key, value) -> {
                if (value instanceof Map) {
                    flattenMap(properties, path + key + ".", (Map<String, Object>) value);
                } else {
                    properties.put(path + key, value.toString());
                }
            });
        }
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");
                return headerLang == null || headerLang.isEmpty()
                        ? Locale.getDefault()
                        : Locale.forLanguageTag(headerLang);
            }
        };
    }
}