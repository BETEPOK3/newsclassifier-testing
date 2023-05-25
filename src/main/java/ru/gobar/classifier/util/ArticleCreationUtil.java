package ru.gobar.classifier.util;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import ru.gobar.classifier.api.client.ArticleCreateClient;
import ru.gobar.classifier.api.request.ArticleCreateRequest;
import ru.gobar.classifier.dao.PostgresArticleDao;
import ru.gobar.classifier.model.Article;

public class ArticleCreationUtil {

    private static final ArticleCreateClient createClient = new ArticleCreateClient();

    @SneakyThrows
    @Step("Загрузка статьи")
    public static Article createArticle(Article article) {
        article.setId(createClient.post(ArticleCreateRequest.instance(article)).
                assertThat().statusCode(HttpStatus.SC_OK).extract().path("id"));

        new PostgresArticleDao().getById(article.getId()).getCategories().forEach(oc -> {
            article.getCategories().forEach(ic -> {
                if (oc.getName().equals(ic.getName())) {
                    ic.setId(oc.getId());
                }
            });
        });

        return article;
    }
}
