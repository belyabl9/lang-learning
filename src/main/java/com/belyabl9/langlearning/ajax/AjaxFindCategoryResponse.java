package com.belyabl9.langlearning.ajax;

import com.belyabl9.langlearning.dto.CategoryDto;

public class AjaxFindCategoryResponse extends AjaxResponseBody {

    private final CategoryDto category;

    public AjaxFindCategoryResponse(CategoryDto category) {
        super(true, null);
        this.category = category;
    }
    
    public AjaxFindCategoryResponse(boolean success, String message) {
        super(success, message);
        this.category = null;
    }

    public CategoryDto getCategory() {
        return category;
    }
}
