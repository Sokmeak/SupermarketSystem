package com.tutorial.tutorial.Product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tutorial.tutorial.Category.entity.CategoryEntity;
import com.tutorial.tutorial.Product.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    ProductEntity findByProductCode(String productCode);
    
    List<ProductEntity> findByCategory(CategoryEntity category);
    
    @Query("SELECT p FROM ProductEntity p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProductEntity> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM ProductEntity p WHERE p.category.id = :categoryId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<ProductEntity> searchByCategoryAndKeyword(@Param("categoryId") Long categoryId, 
                                                    @Param("keyword") String keyword);
}
