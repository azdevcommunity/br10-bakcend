package fib.br10.dto.cache;

import jakarta.persistence.Cacheable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Cacheable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CacheToken implements Serializable {
    Integer tokenId;
    Integer tokenType;
}
