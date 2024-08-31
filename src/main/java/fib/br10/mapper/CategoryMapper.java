package fib.br10.mapper;

import fib.br10.dto.category.request.CreateCategoryRequest;
import fib.br10.dto.category.request.UpdateCategoryRequest;
import fib.br10.dto.category.response.CategoryResponse;
import fib.br10.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

Category createCategoryToCategory(@MappingTarget Category category, CreateCategoryRequest request);
Category updateCategoryToCategory(@MappingTarget Category category, UpdateCategoryRequest request);

CategoryResponse  categoryToCategoryResponse(Category category);
}
