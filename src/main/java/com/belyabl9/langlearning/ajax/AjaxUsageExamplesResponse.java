package com.belyabl9.langlearning.ajax;

import java.util.Collection;
import java.util.Collections;

public class AjaxUsageExamplesResponse extends AjaxResponseBody {
    private Collection<String> sentences;

    public static AjaxUsageExamplesResponse failure(String message) {
        return new AjaxUsageExamplesResponse(false, message, Collections.emptyList());
    }

    public static AjaxUsageExamplesResponse create(Collection<String> sentences) {
        return new AjaxUsageExamplesResponse(true, null, sentences);
    }

    private AjaxUsageExamplesResponse(boolean success, String message, Collection<String> sentences) {
        super(success, message);
        this.sentences = sentences;
    }

    public Collection<String> getSentences() {
        return sentences;
    }

    public void setSentences(Collection<String> sentences) {
        this.sentences = sentences;
    }
}