package fib.br10.service.abstracts;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.category.request.CreateCategoryRequest;
import fib.br10.dto.category.request.UpdateCategoryRequest;
import fib.br10.dto.category.response.CategoryResponse;
import fib.br10.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request, Long userId);

    CategoryResponse update(UpdateCategoryRequest request, Long userId);

    Long delete(RequestById request, Long userId);

    List<CategoryResponse> findAllCategories(Long userId);

    Category findById(Long id);

    void validateCategoryExists(Long id);
}
