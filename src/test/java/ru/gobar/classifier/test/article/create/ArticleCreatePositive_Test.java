package ru.gobar.classifier.test.article.create;

import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.dao.PostgresArticleDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.ArticleUtil;
import ru.gobar.classifier.util.AssertUtil;


import java.util.HashSet;

import static ru.gobar.classifier.Endpoints.ARTICLE_CREATE;

@DisplayName(ARTICLE_CREATE + " - создание статей")
public class ArticleCreatePositive_Test extends AbstractTest {

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989482")
    @DisplayName("[T3] /article/create Успешное создание статьи с базовыми параметрами")
    void testBase() {
        Article art = RandomArticleGenerator.randomArticleBase();
        art.setKeywords(null);
        ArticleUtil.createArticle(art);
        art.setKeywords(new HashSet<>());

        Article actual = new PostgresArticleDao().getById(art.getId());
        AssertUtil.assertEquals(art, actual);
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989483")
    @DisplayName("[T4] /article/create Успешное создание статьи со всеми параметрами")
    void testFull() {
        Article art = RandomArticleGenerator.randomArticleWithAllFields();
        ArticleUtil.createArticle(art);

        Article actual = new PostgresArticleDao().getById(art.getId());
        AssertUtil.assertEquals(art, actual);
    }
}
