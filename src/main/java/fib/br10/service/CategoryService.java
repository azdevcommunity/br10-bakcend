package fib.br10.service;

import fib.br10.core.dto.RequestById;
import fib.br10.dto.category.request.CreateCategoryRequest;
import fib.br10.dto.category.request.UpdateCategoryRequest;
import fib.br10.dto.category.response.CategoryResponse;
import fib.br10.entity.Category;
import fib.br10.exception.category.CategoryExistsSameNameException;
import fib.br10.exception.category.CategoryHaveProductException;
import fib.br10.exception.category.CategoryNotFoundException;
import fib.br10.mapper.CategoryMapper;
import fib.br10.repository.CategoryRepository;
import fib.br10.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fib.br10.utility.CacheKeys.CATEGORIES;
import static fib.br10.utility.CacheKeys.PRODUCTS;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    UserService userService;
    ProductRepository productRepository;

    @CacheEvict(value = CATEGORIES, key = "#userId")
    public Long create(CreateCategoryRequest request, Long userId) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryExistsSameNameException();
        }

        userService.existsByIdAndUserRoleSpecialist(userId);

        Category category = new Category();
        category = categoryMapper.createCategoryToCategory(category, request);
        category.setSpecialistUserId(userId);

        categoryRepository.save(category);

        return category.getId();
    }

    @CacheEvict(value = CATEGORIES, key = "#userId")
    public Long update(UpdateCategoryRequest request, Long userId) {
        if (categoryRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new CategoryExistsSameNameException();
        }

        userService.existsByIdAndUserRoleSpecialist(userId);

        Category category = findById(request.getId());

        userService.validateSpecialist(category.getSpecialistUserId(), userId);

        category = categoryMapper.updateCategoryToCategory(category, request);

        categoryRepository.save(category);

        return category.getId();
    }

    @CacheEvict(value = CATEGORIES, key = "#userId")
    public Long delete(RequestById request, Long userId) {
        if (productRepository.existsByCategoryId(request.getId())) {
            throw new CategoryHaveProductException();
        }

        Category category = findById(request.getId());

        userService.validateSpecialist(category.getSpecialistUserId(), userId);

        categoryRepository.delete(category);

        return category.getId();
    }

    @Cacheable(value = PRODUCTS, key = "#userId")
    public List<CategoryResponse> findAllCategories(Long userId) {
        return new ArrayList<>(categoryRepository.findAllSpecialistCategories(userId));
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    public void validateCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException();
        }
    }
}
