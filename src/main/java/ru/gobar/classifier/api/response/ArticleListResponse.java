package ru.gobar.classifier.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.gobar.classifier.model.Article;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ArticleListResponse {

    private List<ArticleGetResponse> articles;
    @JsonProperty("articles_count")
    private int count;

    public static ArticleListResponse instance(List<Article> articles) {
        List<ArticleGetResponse> articleResponses = new ArrayList<>();
        articles.forEach(a -> articleResponses.add(ArticleGetResponse.instance(a)));

        return new ArticleListResponse().setArticles(articleResponses).setCount(articleResponses.size());
    }

}
