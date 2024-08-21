package fib.br10.repository;

import fib.br10.dto.category.response.CategoryResponse;
import fib.br10.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> ,
        QuerydslPredicateExecutor<Category> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,Long id);

    @Query("""
            Select new fib.br10.dto.category.response.CategoryResponse( 
             c.id,c.name,c.description,c.specialistUserId
              ) from Category c 
            where c.specialistUserId=:specialistUserId
            """)
    List<CategoryResponse> findAllSpecialistCategories(Long specialistUserId);
}
