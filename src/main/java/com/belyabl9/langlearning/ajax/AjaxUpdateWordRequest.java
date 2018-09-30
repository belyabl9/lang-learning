package com.belyabl9.langlearning.ajax;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AjaxUpdateWordRequest {
    private static final Splitter USAGE_EXAMPLES_SPLITTER = Splitter.on(CharMatcher.anyOf(".!?\n")).trimResults().omitEmptyStrings();
    
    private long id;
    private String original;
    private String translation;
    private String usageExamples;
    private String associationImgBase64;

    public AjaxUpdateWordRequest() {
    }

    public AjaxUpdateWordRequest(long id, String original, String translation, String usageExamples, String associationImgBase64) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.usageExamples = usageExamples;
        this.associationImgBase64 = associationImgBase64;
    }

    public long getId() {
        return id;
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
        return new ArrayList<>(USAGE_EXAMPLES_SPLITTER.splitToList(usageExamples));
    }

    public String getAssociationImgBase64() {
        return associationImgBase64;
    }
}
