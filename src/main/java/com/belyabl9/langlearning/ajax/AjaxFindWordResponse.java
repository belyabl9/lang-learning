package com.belyabl9.langlearning.ajax;

import com.belyabl9.langlearning.dto.WordDto;

public class AjaxFindWordResponse extends AjaxResponseBody {

    private final WordDto word;

    public AjaxFindWordResponse(WordDto word) {
        super(true, null);
        this.word = word;
    }
    
    public AjaxFindWordResponse(boolean success, String message) {
        super(success, message);
        this.word = null;
    }

    public WordDto getWord() {
        return word;
    }
}
