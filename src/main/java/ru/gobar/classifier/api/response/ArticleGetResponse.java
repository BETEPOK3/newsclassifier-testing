package ru.gobar.classifier.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class ArticleGetResponse {

    @JsonProperty("article_id")
    private int id;
    @JsonProperty("article_title")
    private String title;
    @JsonProperty("article_author")
    private String author;
    @JsonProperty("article_keywords")
    public Set<String> keywords;
    @JsonProperty("article_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonProperty("article_text")
    private String text;
    private Set<Category> categories;

    public static ArticleGetResponse instance(Article article) {
        return new ArticleGetResponse().setId(article.getId())
                .setTitle(article.getTitle())
                .setAuthor(article.getAuthor())
                .setKeywords(article.getKeywords())
                .setDate(article.getDate())
                .setText(article.getText())
                .setCategories(article.getCategories().stream().map(Category::new).collect(Collectors.toSet()));
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Category {
        @JsonProperty("category_id")
        private int id;
        @JsonProperty("category_name")
        private String name;

        private Category(ru.gobar.classifier.model.Category category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
