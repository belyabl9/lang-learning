package com.belyabl9.langlearning.ajax;

import lombok.Data;

public @Data class AjaxAddCategoryRequest {
    private Long userId;
    private String categoryName;
}
