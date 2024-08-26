package fib.br10.controller;

import fib.br10.core.dto.RequestById;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.dto.product.request.CreateProductRequest;
import fib.br10.dto.product.request.UpdateProductRequest;
import fib.br10.dto.product.response.ProductResponse;
import fib.br10.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasRole('SPECIALIST')")
public class ProductController {
    ProductService productService;
    RequestContextProvider provider;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.create(request, provider.getUserId()));
    }

    @PutMapping
    public ResponseEntity<Long> update(@RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(request, provider.getUserId()));
    }

    @DeleteMapping
    public ResponseEntity<Long> delete(@RequestBody @Valid RequestById request) {
        return ResponseEntity.ok(productService.delete(request, provider.getUserId()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<ProductResponse>> findAllCategories(@PathVariable("specialistId") @Valid Long id) {
        return ResponseEntity.ok(productService.findAllProducts(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllCategories() {
        return ResponseEntity.ok(productService.findAllProducts(provider.getUserId()));
    }
}