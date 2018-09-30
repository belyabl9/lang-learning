package com.belyabl9.langlearning.controller;

import com.belyabl9.langlearning.domain.Image;
import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.service.AuthService;
import com.belyabl9.langlearning.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;
    
    @Autowired
    private AuthService authService;
    
    @RequestMapping(value = "/image/{imageId}")
    @ResponseBody
    public byte[] helloWorld(@PathVariable long imageId, Principal principal) {
        Image image = imageService.findById(imageId);
        User user = authService.extractUserFromAuthInfo(principal);

        if (image == null ) {
            throw new IllegalArgumentException("Image with the specified id does not exist.");
        }
        if (user.getId() != image.getWord().getCategory().getUser().getId()) {
            throw new IllegalArgumentException("The requested image must belong to the current user.");
        }
        
        return image.getData();
    }
    
}
