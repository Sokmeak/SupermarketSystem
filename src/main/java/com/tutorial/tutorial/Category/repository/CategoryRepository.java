package com.tutorial.tutorial.Category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tutorial.tutorial.Category.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByName(String name);
    
    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.products")
    Page<CategoryEntity> findAllWithProducts(Pageable pageable);
}
