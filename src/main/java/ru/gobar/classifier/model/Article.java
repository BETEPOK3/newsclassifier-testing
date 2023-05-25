package ru.gobar.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @JsonProperty("article_id")
    private int id;
    @JsonProperty("article_title")
    private String title;
    @JsonProperty("article_author")
    private String author;
    @JsonProperty("article_keywords")
    public Set<String> keywords;
    @JsonProperty("article_date")
    private LocalDate date;
    @JsonProperty("article_text")
    private String text;
    private Set<Category> categories;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Article)) {
            return false;
        }

        Article a = (Article) object;

        boolean res = true;
        return this.id == a.id && this.title.equals(a.title) && Objects.equals(this.author, a.author) &&
                a.keywords.containsAll(this.keywords) && this.keywords.containsAll(a.keywords) &&
                this.date.isEqual(a.date) && Arrays.deepEquals(this.categories.toArray(), a.categories.toArray());
    }

    public enum Fields {
        article_id,
        article_title,
        article_author,
        article_keywords,
        article_date,
        article_text,
        article_categories
    }
}
