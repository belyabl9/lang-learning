package com.belyabl9.langlearning.ajax;

import lombok.Data;

public @Data class AjaxUpdateCategoryRequest {
    private long id;
    private String categoryName;
}
