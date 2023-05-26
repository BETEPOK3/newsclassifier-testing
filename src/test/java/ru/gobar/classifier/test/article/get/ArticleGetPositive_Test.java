package ru.gobar.classifier.test.article.get;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleGetClient;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.ArticleUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.util.Map;

import static ru.gobar.classifier.Endpoints.ARTICLE_GET;

@DisplayName(ARTICLE_GET + " - получение статьи")
public class ArticleGetPositive_Test extends AbstractTest {

    private final ArticleGetClient client = new ArticleGetClient();

    private Map<String, Article> getArticles() {
        return Map.of("Получение базовой статьи",
                ArticleUtil.createArticle(RandomArticleGenerator.randomArticleBase()),
                "Получение статьи со всеми полями",
                ArticleUtil.createArticle(RandomArticleGenerator.randomArticleWithAllFields()));
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989490")
    @DisplayName("[T9] /article Успешное получение статьи")
    void testBase() {
        AllureStepUtil stepper = new AllureStepUtil();
        getArticles().forEach((k, a) -> stepper.runStep(k, () -> {
            Article res = client.get(a.getId()).assertThat().statusCode(HttpStatus.SC_OK).extract().body().as(Article.class);
            AssertUtil.assertEquals(a, res);
        }));
        stepper.check();
    }
}
