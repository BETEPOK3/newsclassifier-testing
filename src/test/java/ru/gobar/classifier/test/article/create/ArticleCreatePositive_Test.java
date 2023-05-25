package ru.gobar.classifier.test.article.create;

import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.dao.PostgresArticleDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.ArticleCreationUtil;
import ru.gobar.classifier.util.AssertUtil;


import java.util.HashSet;

import static ru.gobar.classifier.Endpoints.ARTICLE_CREATE;

@DisplayName(ARTICLE_CREATE)
public class ArticleCreatePositive_Test extends AbstractTest {

    @Test
    @TmsLink("...")
    @DisplayName("/article/create Успешное создание статьи с базовыми параметрами")
    void testBase() {
        Article art = RandomArticleGenerator.randomArticleBase();
        art.setKeywords(null);
        ArticleCreationUtil.createArticle(art);
        art.setKeywords(new HashSet<>());

        Article actual = new PostgresArticleDao().getById(art.getId());
        AssertUtil.assertEquals(art, actual);
    }

    @Test
    @TmsLink("...")
    @DisplayName("/article/create Успешное создание статьи со всеми параметрами")
    void testFull() {
        Article art = RandomArticleGenerator.randomArticleWithAllFields();
        ArticleCreationUtil.createArticle(art);

        Article actual = new PostgresArticleDao().getById(art.getId());
        AssertUtil.assertEquals(art, actual);
    }
}
