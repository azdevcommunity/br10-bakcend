package fib.br10.dto.history.customer.response;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ServiceResponseDTO {
    private Long id;
    private Long specialistUserId;
    private Integer duration;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    @QueryProjection
    public ServiceResponseDTO(Long id, Long specialistUserId, Integer duration, String name, BigDecimal price,
                              String description, String imageUrl) {
        this.id = id;
        this.specialistUserId = specialistUserId;
        this.duration = duration;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}