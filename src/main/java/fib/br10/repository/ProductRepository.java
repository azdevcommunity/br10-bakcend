package fib.br10.repository;

import fib.br10.dto.product.response.ProductResponse;
import fib.br10.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    boolean existsByCategoryId(Long categoryId);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
            select new fib.br10.dto.product.response.ProductResponse(
            p.id,p.name,p.description,p.price,p.specialistUserId,p.categoryId,c.name,i.path)
            from Product p
            left join Category c on p.categoryId = c.id
            left join Image  i on p.imageId = i.id
            where p.specialistUserId =:id
            order by p.createdDate desc
            """)
    List<ProductResponse> findAllProducts(Long id);

}
