package com.belyabl9.langlearning.ajax;

import lombok.Data;

public @Data class AjaxAddSentenceExampleRequest {
    private long wordId;
    private String sentence;
}