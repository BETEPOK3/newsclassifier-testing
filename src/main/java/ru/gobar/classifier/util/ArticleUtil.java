package ru.gobar.classifier.util;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import ru.gobar.classifier.api.client.ArticleCreateClient;
import ru.gobar.classifier.api.client.ArticleGetClient;
import ru.gobar.classifier.api.client.ArticleUpdateClient;
import ru.gobar.classifier.api.request.ArticleCreateRequest;
import ru.gobar.classifier.api.response.ArticleGetResponse;
import ru.gobar.classifier.dao.PostgresArticleDao;
import ru.gobar.classifier.model.Article;

public class ArticleUtil {

    private static final ArticleCreateClient createClient = new ArticleCreateClient();
    private static final ArticleUpdateClient updateClient = new ArticleUpdateClient();

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

    @SneakyThrows
    @Step("Обновление статьи")
    public static Article updateArticle(Article article, int id) {
        article.setId(id);
        ArticleGetResponse actual = updateClient.post(ArticleCreateRequest.instance(article), id).
                assertThat().statusCode(HttpStatus.SC_OK).extract().as(ArticleGetResponse.class);

        new PostgresArticleDao().getById(article.getId()).getCategories().forEach(oc -> {
            article.getCategories().forEach(ic -> {
                if (oc.getName().equals(ic.getName())) {
                    ic.setId(oc.getId());
                }
            });
        });

        AssertUtil.assertEquals(ArticleGetResponse.instance(article), actual);

        return article;
    }
}
