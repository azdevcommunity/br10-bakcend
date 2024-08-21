package fib.br10.mapper;

import fib.br10.dto.product.request.CreateProductRequest;
import fib.br10.dto.product.request.UpdateProductRequest;
import fib.br10.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product createProductToProduct(@MappingTarget Product product, CreateProductRequest request);
    Product updateProductToProduct(@MappingTarget Product product, UpdateProductRequest request);
}
