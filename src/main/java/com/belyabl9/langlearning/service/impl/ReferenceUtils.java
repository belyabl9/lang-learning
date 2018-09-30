package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.Category;
import com.google.common.base.Splitter;

import java.util.Base64;
import java.util.List;

public class ReferenceUtils {
    
    private static final Splitter REFERENCE_FIELD_SPLITTER = Splitter.on(";").trimResults().omitEmptyStrings(); 
    
    public static class Reference {
        private final long userId;
        private final long categoryId;

        public Reference(long userId, long categoryId) {
            this.userId = userId;
            this.categoryId = categoryId;
        }

        public long getUserId() {
            return userId;
        }

        public long getCategoryId() {
            return categoryId;
        }
    }
    
    public static String encode(Category category) {
        return Base64.getEncoder().encodeToString(
                (category.getId().toString() + ";" + category.getUser().getId().toString()).getBytes()
        );
    }
    
    private static Reference decode(String referenceStr) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(referenceStr);
        String decodedStr = new String(decodedBytes);
        List<String> fields = REFERENCE_FIELD_SPLITTER.splitToList(decodedStr);
        if (fields.size() != 2) {
            throw new RuntimeException("Reference must contain two fields.");
        }
        return new Reference(Long.parseLong(fields.get(0)), Long.parseLong(fields.get(1)));
    }
    
}
