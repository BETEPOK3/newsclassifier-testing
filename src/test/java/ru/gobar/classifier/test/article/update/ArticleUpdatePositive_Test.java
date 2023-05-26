package ru.gobar.classifier.test.article.update;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleUpdateClient;
import ru.gobar.classifier.dao.PostgresArticleDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.ArticleUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.util.Map;

import static ru.gobar.classifier.Endpoints.ARTICLE_UPDATE;

@DisplayName(ARTICLE_UPDATE + " - обновление статьи")
public class ArticleUpdatePositive_Test extends AbstractTest {

    private final ArticleUpdateClient client = new ArticleUpdateClient();

    private Map<String, Article> getArticles() {
        return Map.of("Получение базовой статьи",
                ArticleUtil.createArticle(RandomArticleGenerator.randomArticleBase()),
                "Получение статьи со всеми полями",
                ArticleUtil.createArticle(RandomArticleGenerator.randomArticleWithAllFields()));
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989556")
    @DisplayName("[T26] /article/update Успешное получение статьи")
    void testBase() {
        AllureStepUtil stepper = new AllureStepUtil();
        getArticles().forEach((k, a) -> stepper.runStep(k, () -> {
            Article res = client.get(a.getId()).assertThat().statusCode(HttpStatus.SC_OK).extract().body().as(Article.class);
            AssertUtil.assertEquals(a, res);
        }));
        stepper.check();
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989558")
    @DisplayName("[T27] /article/update Успешное обновление статьи с базовыми параметрами")
    void updateBase() {
        Article art = RandomArticleGenerator.randomArticleBase();
        ArticleUtil.createArticle(art);
        Article newArt = RandomArticleGenerator.randomArticleBase();

        ArticleUtil.updateArticle(newArt, art.getId());

        Article actual = new PostgresArticleDao().getById(newArt.getId());
        AssertUtil.assertEquals(newArt, actual);
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989559")
    @DisplayName("[T28] /article/update Успешное обновление статьи со всеми параметрами")
    void updateFull() {
        Article art = RandomArticleGenerator.randomArticleWithAllFields();
        ArticleUtil.createArticle(art);
        Article newArt = RandomArticleGenerator.randomArticleWithAllFields();

        ArticleUtil.updateArticle(newArt, art.getId());

        Article actual = new PostgresArticleDao().getById(newArt.getId());
        AssertUtil.assertEquals(newArt, actual);
    }
}
