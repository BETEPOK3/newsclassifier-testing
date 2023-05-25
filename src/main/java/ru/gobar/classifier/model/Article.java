package ru.gobar.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
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

    public enum Fields{
        article_id,
        article_title,
        article_author,
        article_keywords,
        article_date,
        article_text,
        article_categories
    }
}
