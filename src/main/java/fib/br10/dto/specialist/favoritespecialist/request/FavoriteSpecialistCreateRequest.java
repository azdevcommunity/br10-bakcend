package fib.br10.dto.specialist.favoritespecialist.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteSpecialistCreateRequest {
    private Long specialistId;
}