package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Image;
import com.belyabl9.langlearning.repository.ImageRepository;
import com.belyabl9.langlearning.service.ImageService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Override
    public Image findById(long id) {
        return imageRepository.findOne(id);
    }

    @Override
    public Image insert(@NonNull Image image) {
        return imageRepository.saveAndFlush(image);
    }

    @Override
    public void remove(@NonNull Image image) {
        image.getWord().setAssociationImg(null);
        imageRepository.delete(image);
    }

}
