package com.belyabl9.langlearning.ajax;

public class AjaxReferenceResponse extends AjaxResponseBody {

    private final String reference;

    public AjaxReferenceResponse(String reference) {
        super(true, null);
        this.reference = reference;
    }
    
    public AjaxReferenceResponse(boolean success, String message) {
        super(success, message);
        this.reference = null;
    }

    public String getReference() {
        return reference;
    }
}
