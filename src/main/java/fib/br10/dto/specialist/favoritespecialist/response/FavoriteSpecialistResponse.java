package fib.br10.dto.specialist.favoritespecialist.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteSpecialistResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long specialistId;
    private String specialistName;
}