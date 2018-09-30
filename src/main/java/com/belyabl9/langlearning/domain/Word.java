package com.belyabl9.langlearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "original"})})
public class Word extends BaseEntity implements Serializable, Comparable<Word> {
    @NotBlank
    private String original;
    @NotBlank
    private String translation;
    
    @JsonIgnore
    @ManyToOne
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
    private List<String> sentenceExamples;

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

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getSentenceExamples() {
        return sentenceExamples;
    }

    public void setSentenceExamples(List<String> sentenceExamples) {
        this.sentenceExamples = sentenceExamples;
    }

    public Image getAssociationImg() {
        return associationImg;
    }

    public void setAssociationImg(Image associationImg) {
        this.associationImg = associationImg;
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
