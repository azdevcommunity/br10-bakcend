package fib.br10.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fib.br10.core.entity.BaseEntityListener;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.core.utility.RandomUtil;
import fib.br10.core.utility.RequestContextEnum;
import fib.br10.core.utility.RequestContext;
import fib.br10.entity.specialist.Speciality;
import fib.br10.entity.user.RoleEnum;
import fib.br10.entity.user.User;
import fib.br10.repository.SpecialityRepository;
import fib.br10.repository.UserRepository;
import fib.br10.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


import java.util.List;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationBeanConfiguration {

    SpecialityRepository specialityRepository;
    UserRepository userRepository;
    UserRoleRepository userRoleRepository;
    RequestContextProvider provider;

    @Bean
    public CommandLineRunner commandLineRunner() {
        provider.setActivityId( RandomUtil.getUUIDAsString());
        return (args) -> {
            if(!userRoleRepository.existsByRoleId(RoleEnum.ADMIN.getValue())){
               final User user= User.builder()
                        .phoneNumber("0551112211")
                        .name("admin")
                        .username("admin")
                        .password("admin")
                        .build();
                userRepository.saveAndFlush(user);

            }

            if (specialityRepository.count() == 0) {
                final List<Speciality> specialities = List.of(
                        new Speciality("berber", "berber_en", "berber_ru"),
                        new Speciality("hekim", "hekim_en", "hekim_ru"),
                        new Speciality("psixoloq", "psixoloq_en", "psixoloq_ru"),
                        new Speciality("parexmaxr", "parexmaxr_en", "parexmaxr_ru")
                );
                specialityRepository.saveAllAndFlush(specialities);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
