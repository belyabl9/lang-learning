package com.belyabl9.langlearning.ajax;

import java.util.Collections;
import java.util.List;

public class AjaxUsageExamplesResponse extends AjaxResponseBody {
    private List<String> sentences;

    public static AjaxUsageExamplesResponse failure(String message) {
        return new AjaxUsageExamplesResponse(false, message, Collections.emptyList());
    }

    public static AjaxUsageExamplesResponse create(List<String> sentences) {
        return new AjaxUsageExamplesResponse(true, null, sentences);
    }

    private AjaxUsageExamplesResponse(boolean success, String message, List<String> sentences) {
        super(success, message);
        this.sentences = sentences;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }
}