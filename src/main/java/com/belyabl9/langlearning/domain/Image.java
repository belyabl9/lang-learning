package com.belyabl9.langlearning.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Image extends BaseEntity implements Serializable {
    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;
    
    public Image() {
    }
    
    public Image(byte[] data, Word word) {
        this.data = data;
        this.word = word;
    }

    public String getUrl() {
        if (getId() == null) {
            throw new RuntimeException("Image must be saved to a database before accessing the url.");
        }
        return "/image/" + getId();
    }
}
