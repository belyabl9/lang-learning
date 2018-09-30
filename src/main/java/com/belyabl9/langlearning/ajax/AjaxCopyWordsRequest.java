package com.belyabl9.langlearning.ajax;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class AjaxCopyWordsRequest {
    private static final Splitter ID_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    
    private String wordsIds;
    private Long categoryId;

    public AjaxCopyWordsRequest() {
    }

    public AjaxCopyWordsRequest(String wordsIds, Long categoryId) {
        this.wordsIds = wordsIds;
        this.categoryId = categoryId;
    }

    public List<Long> getWordsIds() {
        return ID_SPLITTER.splitToList(wordsIds).stream().map(Long::valueOf).collect(Collectors.toList());
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
