package fib.br10.service;


import fib.br10.core.dto.RequestById;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.product.request.CreateProductRequest;
import fib.br10.dto.product.request.UpdateProductRequest;
import fib.br10.dto.product.response.ProductResponse;
import fib.br10.entity.Product;
import fib.br10.exception.category.CategoryHaveProductException;
import fib.br10.exception.product.ProductExistsSameNameException;
import fib.br10.exception.product.ProductNotFoundException;
import fib.br10.mapper.ProductMapper;
import fib.br10.repository.ProductRepository;
import fib.br10.utility.Messages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static fib.br10.utility.CacheKeys.PRODUCTS;

@Validated
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    UserService userService;
    CategoryService categoryService;

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public Long create(CreateProductRequest request, Long userId ) {
        if (productRepository.existsByName(request.getName())) {
            throw new BaseException("product exists same name");
        }

        categoryService.validateCategoryExists(request.getCategoryId());
        userService.existsByIdAndUserRoleSpecialist(userId);

        Product product = new Product();
        product = productMapper.createProductToProduct(product, request);
        product.setSpecialistUserId(userId);

        productRepository.save(product);

        return product.getId();
    }

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public Long update(UpdateProductRequest request, Long userId) {
        if (productRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new ProductExistsSameNameException();
        }

        userService.existsByIdAndUserRoleSpecialist(userId);

        //dont need this i add PREAUTHORIES to Controller
//        categoryService.validateCategoryExists(request.getCategoryId());

        Product product = findById(request.getId());

        userService.validateSpecialist(product.getSpecialistUserId(), userId);

        product = productMapper.updateProductToProduct(product, request);

        productRepository.save(product);

        return product.getId();
    }

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public Long delete(RequestById request,Long userId ) {
        if (productRepository.existsById(request.getId())) {
            throw new CategoryHaveProductException();
        }

        Product product = findById(request.getId());

        userService.validateSpecialist(product.getSpecialistUserId(), userId);

        productRepository.delete(product);

        return product.getId();
    }

    @Cacheable(value = PRODUCTS, key = "#id")
    public List<ProductResponse> findAllProducts(Long id ) {
        return new ArrayList<>(productRepository.findAllProducts(id));
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }
}
