package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.domain.InternalUser;
import com.belyabl9.langlearning.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalUserRepository extends JpaRepository<InternalUser, Long> {
    User findByLogin(String login);
}
