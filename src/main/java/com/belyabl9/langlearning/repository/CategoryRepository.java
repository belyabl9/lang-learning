package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameAndUser(String name, User user);
    Category findBySharedReference(String sharedReference);
    List<Category> findAllByUser(User user);
    List<Category> findAllByUserIsNull();
}
