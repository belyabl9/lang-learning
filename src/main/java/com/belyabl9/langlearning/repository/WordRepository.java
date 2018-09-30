package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.domain.Category;
import com.belyabl9.langlearning.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Word findByOriginalAndCategory(String original, Category category);
    
    List<Word> findAllByCategoryOrderByPriorityDesc(Category category);

    @Transactional
    @Modifying
    @Query("update Word u set priority = priority + 1 where id in (:wordIds)")
    void incrementPriority(@Param("wordIds") Set<Long> wordIds);

    @Transactional
    @Modifying
    @Query("update Word u set priority = priority - 1 where id in (:wordIds) and priority > 0")
    void decrementPriority(@Param("wordIds") Set<Long> wordIds);
}
