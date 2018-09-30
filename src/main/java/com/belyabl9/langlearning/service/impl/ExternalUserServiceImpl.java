package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.ExternalUser;
import com.belyabl9.langlearning.domain.SocialProvider;
import com.belyabl9.langlearning.repository.ExternalUserRepository;
import com.belyabl9.langlearning.service.ExternalUserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserServiceImpl implements ExternalUserService {

    @Autowired
    private ExternalUserRepository repository; 
    
    @Override
    public ExternalUser findByExternalIdAndProvider(@NonNull String externalId,
                                                    @NonNull SocialProvider socialProvider) {
        return repository.findByExternalIdAndSocialProvider(externalId, socialProvider);
    }

    @Override
    public void remove(long id) {
        repository.delete(id);
    }
}
