package com.belyabl9.langlearning.repository;

import com.belyabl9.langlearning.domain.ExternalUser;
import com.belyabl9.langlearning.domain.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
    ExternalUser findByExternalIdAndSocialProvider(String externalId, SocialProvider socialProvider);
}
