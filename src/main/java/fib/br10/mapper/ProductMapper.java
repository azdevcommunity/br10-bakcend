package fib.br10.mapper;

import fib.br10.dto.product.request.CreateProductRequest;
import fib.br10.dto.product.request.UpdateProductRequest;
import fib.br10.dto.product.response.ProductResponse;
import fib.br10.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product createProductToProduct( CreateProductRequest request);

    Product updateProductToProduct(@MappingTarget Product product, UpdateProductRequest request);

    @Mapping(target = "categoryName", source = "categoryName")
    @Mapping(target = "image", source = "image")
    ProductResponse productToProductResponse(Product product, String categoryName, String image);

    @Mapping(target = "categoryName", source = "categoryName")
    ProductResponse productToProductResponse(Product product, String categoryName);
}
