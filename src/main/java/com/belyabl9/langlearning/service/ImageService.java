package com.belyabl9.langlearning.service;

import com.belyabl9.langlearning.domain.Image;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Service
public interface ImageService {
    
    @Nullable
    Image findById(long id);

    @NotNull
    Image insert(@NotNull Image image);
    
    void remove(@NotNull Image image);
    
}
