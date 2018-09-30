package com.belyabl9.langlearning.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "name"})})
public class Category extends BaseEntity implements Serializable {
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> words;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private String sharedReference;
    
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
    
    public Category(String name, List<Word> words) {
        this.name = name;
        this.words = words;
    }
    
    public Category(String name, List<Word> words, User user) {
        this.name = name;
        this.words = words;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
//        if (words != null && words.size() > 1) {
//            this.words = words.stream().sorted().collect(Collectors.toList());
//        } else {
            this.words = words;
//        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isBuiltIn() {
        return user == null;
    }

    public String getSharedReference() {
        return sharedReference;
    }

    public void setSharedReference(String sharedReference) {
        this.sharedReference = sharedReference;
    }

    public boolean isShared() {
        return sharedReference != null;
    }
    
    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Category category = (Category) o;
//
//        if (name != null ? !name.equals(category.name) : category.name != null) return false;
//        if (words != null ? !words.equals(category.words) : category.words != null) return false;
//        return user != null ? user.equals(category.user) : category.user == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = name != null ? name.hashCode() : 0;
//        result = 31 * result + (words != null ? words.hashCode() : 0);
//        result = 31 * result + (user != null ? user.hashCode() : 0);
//        return result;
//    }
}
