package fib.br10.service;


import fib.br10.core.dto.RequestById;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.image.response.CreateImageResponse;
import fib.br10.dto.product.request.CreateProductRequest;
import fib.br10.dto.product.request.UpdateProductRequest;
import fib.br10.dto.product.response.ProductResponse;
import fib.br10.entity.Category;
import fib.br10.entity.Image;
import fib.br10.entity.Product;
import fib.br10.exception.product.ProductExistsSameNameException;
import fib.br10.exception.product.ProductNotFoundException;
import fib.br10.mapper.ProductMapper;
import fib.br10.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    ImageService imageService;

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public ProductResponse create(CreateProductRequest request, Long userId) {
        if (productRepository.existsByName(request.getName())) {
            throw new BaseException("product exists same name");
        }
        if (Objects.isNull(request.getImage())) {
            throw new BaseException("image is required");
        }
        Category category = categoryService.findById(request.getCategoryId());
        CreateImageResponse imageResponse = imageService.create(request.getImage());

        Product product = productMapper.createProductToProduct(request);
        product.setSpecialistUserId(userId);
        product.setImageId(imageResponse.getId());
        productRepository.save(product);
        return productMapper.productToProductResponse(product, category.getName(), imageResponse.getPath());
    }

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public ProductResponse update(UpdateProductRequest request, Long userId) {
        if (productRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new ProductExistsSameNameException();
        }
        Category category = categoryService.findById(request.getCategoryId());
        Product product = findById(request.getId());
        userService.validateSpecialist(product.getSpecialistUserId(), userId);
        product = productMapper.updateProductToProduct(product, request);
        productRepository.save(product);
        Image image = imageService.findById(product.getImageId());
        ProductResponse response =  productMapper.productToProductResponse(product, category.getName());
       if(Objects.nonNull(image)){
           response.setImage(image.getPath());
       }
        return response;
    }

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public Long delete(RequestById request, Long userId) {
        Product product = findById(request.getId());
        userService.validateSpecialist(product.getSpecialistUserId(), userId);
        productRepository.delete(product);
        return product.getId();
    }

    @Cacheable(value = PRODUCTS, key = "#id")
    public List<ProductResponse> findAllProducts(Long id) {
        return new ArrayList<>(productRepository.findAllProducts(id));
    }

    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        Category category = categoryService.findById(product.getCategoryId());
        Image image = imageService.findById(product.getImageId());
        return productMapper.productToProductResponse(product, category.getName(), image.getPath());
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @CacheEvict(value = PRODUCTS, key = "#userId")
    public ProductResponse updateImage(MultipartFile image, Long productId, Long userId) {
        Product product = findById(productId);
        if (Objects.nonNull(product.getImageId())) {
            imageService.delete(product.getImageId());
        }
        Category category = categoryService.findById(product.getCategoryId());
        CreateImageResponse imageResponse = imageService.create(image);

        product.setImageId(imageResponse.getId());
        productRepository.save(product);
        return productMapper.productToProductResponse(product, category.getName(), imageResponse.getPath());
    }
}
