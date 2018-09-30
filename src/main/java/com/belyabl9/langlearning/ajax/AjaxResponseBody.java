package com.belyabl9.langlearning.ajax;

import lombok.Data;

public @Data class AjaxResponseBody {
    public static final AjaxResponseBody SUCCESS = new AjaxResponseBody(true, null);
    
    private final boolean success;
    private final String message;
    
    public static AjaxResponseBody success() {
        return SUCCESS;
    }
    
    public static AjaxResponseBody failure(String message) {
        return new AjaxResponseBody(false, message);
    }
}
