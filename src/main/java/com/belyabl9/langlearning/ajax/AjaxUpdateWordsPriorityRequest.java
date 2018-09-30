package com.belyabl9.langlearning.ajax;

import lombok.Data;

import java.util.Set;

public @Data class AjaxUpdateWordsPriorityRequest {
    private Set<Long> wrongIds;
    private Set<Long> correctIds;
}