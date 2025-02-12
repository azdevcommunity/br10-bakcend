package fib.br10.controller;


import fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse;
import fib.br10.service.abstracts.FavoriteSpecialistService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/favorite-specialist")
@RequiredArgsConstructor
@Validated
public class FavoriteSpecialistController {
    FavoriteSpecialistService favoriteSpecialistService;


    @PostMapping("/{id}")
    public ResponseEntity<FavoriteSpecialistResponse> create(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(favoriteSpecialistService.create(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable Long id) {
        favoriteSpecialistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
