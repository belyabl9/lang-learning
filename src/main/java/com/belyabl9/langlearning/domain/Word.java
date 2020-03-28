package com.belyabl9.langlearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "original"})})
@Getter
@Setter
public class Word extends BaseEntity implements Serializable, Comparable<Word> {
    @NotBlank
    private String original;
    @NotBlank
    private String translation;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    
    private int priority;

    @OneToOne(mappedBy = "word", cascade = CascadeType.REMOVE)
    private Image associationImg;

    @ElementCollection
    @CollectionTable(name="Synonyms", joinColumns=@JoinColumn(name="word_id"))
    @Column(name="sentence")
    private Set<String> synonyms;

    @ElementCollection
    @CollectionTable(name="Sentences", joinColumns=@JoinColumn(name="word_id"))
    @Column(name="sentence")
    private Collection<String> sentenceExamples;

    public Word() {}

    public Word(String original, String translation, Category category) {
        this(original, translation, category, 0, Collections.emptySet());
    }

    public Word(String original, String translation, Category category, int priority) {
        this(original, translation, category, priority, Collections.emptySet());
    }

    public Word(String original, String translation, Category category, int priority, Set<String> synonyms) {
        this(original, translation, category, priority, synonyms, Collections.emptyList());
    }

    public Word(String original, String translation, Category category, int priority, Set<String> synonyms, List<String> sentenceExamples) {
        this.original = original;
        this.translation = translation;
        this.category = category;
        this.priority = priority;
        this.synonyms = synonyms;
        this.sentenceExamples = sentenceExamples;
    }

    @Override
    public int compareTo(Word o) {
        return Integer.compare(o.getPriority(), getPriority());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (!original.equals(word.original)) return false;
        return translation.equals(word.translation);
    }

    @Override
    public int hashCode() {
        int result = original.hashCode();
        result = 31 * result + translation.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", original='" + original + '\'' +
                ", translation='" + translation + '\'' +
                ", category=" + category +
                ", priority=" + priority +
                ", synonyms=" + synonyms +
                ", sentenceExamples=" + sentenceExamples +
                '}';
    }
}
