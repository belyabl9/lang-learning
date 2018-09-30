package com.belyabl9.langlearning.ajax;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import java.util.Collections;
import java.util.List;

public class AjaxAddWordRequest {
    private static final Splitter USAGE_EXAMPLES_SPLITTER = Splitter.on(CharMatcher.anyOf(".!?\n")).trimResults().omitEmptyStrings();
    
    private String original;
    private String translation;
    private String usageExamples;
    private String associationImgBase64; 
    private Long categoryId;

    public AjaxAddWordRequest() {
    }

    public AjaxAddWordRequest(String original, String translation, String usageExamples, String associationImgBase64, Long categoryId) {
        this.original = original;
        this.translation = translation;
        this.usageExamples = usageExamples;
        this.associationImgBase64 = associationImgBase64;
        this.categoryId = categoryId;
    }

    public String getOriginal() {
        return original;
    }

    public String getTranslation() {
        return translation;
    }

    public List<String> getUsageExamples() {
        if (usageExamples == null) {
            return Collections.emptyList();
        }
        return USAGE_EXAMPLES_SPLITTER.splitToList(usageExamples);
    }

    public String getAssociationImgBase64() {
        return associationImgBase64;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
