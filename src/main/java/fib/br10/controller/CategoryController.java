package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.category.request.CreateCategoryRequest;
import fib.br10.dto.category.request.UpdateCategoryRequest;
import fib.br10.dto.category.response.CategoryResponse;
import fib.br10.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('SPECIALIST')")
public class CategoryController {

    CategoryService categoryService;
    RequestContextProvider provider;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request, provider.getUserId()));
    }

    @PutMapping
    public ResponseEntity<Long> update(@RequestBody @Valid UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(request, provider.getUserId()));
    }

    @DeleteMapping
    public ResponseEntity<Long> update(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(categoryService.delete(request, provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories(provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<CategoryResponse>> findAllCategories(@PathVariable("specialistId") @Valid Long specialistId)  {
        return ResponseEntity.ok(categoryService.findAllCategories(specialistId));
    }
}

