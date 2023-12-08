package ru.gobar.classifier.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ArticleCreateRequest {

    @JsonProperty("article_title")
    private String title;
    @JsonProperty("article_author")
    private String author;
    @JsonProperty("article_keywords")
    public Set<String> keywords;
    @JsonProperty("article_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonInclude
    private LocalDate date;
    @JsonProperty("article_text")
    private String text;
    @JsonProperty("article_categories")
    private Set<String> categories;

    public static ArticleCreateRequest instance(Article article) {
        return new ArticleCreateRequest().setTitle(article.getTitle())
                .setAuthor(article.getAuthor())
                .setKeywords(article.getKeywords())
                .setDate(article.getDate())
                .setText(article.getText())
                .setCategories(article.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
    }
}
