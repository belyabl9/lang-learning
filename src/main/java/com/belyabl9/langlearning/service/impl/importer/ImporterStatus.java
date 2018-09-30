package com.belyabl9.langlearning.service.impl.importer;

public class ImporterStatus {
    private final String message;

    public ImporterStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ImporterStatus{" +
                "message='" + message + '\'' +
                '}';
    }
}
