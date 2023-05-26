package ru.gobar.classifier.test.article.delete;

import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.PropertyReader;
import ru.gobar.classifier.api.client.ArticleDeleteClient;
import ru.gobar.classifier.dao.CategoryDao;
import ru.gobar.classifier.dao.PostgresCategoryDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.database.PostgresContext;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.ArticleCreationUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.util.Set;
import java.util.concurrent.Callable;

import static ru.gobar.classifier.Endpoints.ARTICLE_DELETE;

@DisplayName(ARTICLE_DELETE + " - удаление статей")
public class ArticleDeletePositive_Test extends AbstractTest {

    private final ArticleDeleteClient client = new ArticleDeleteClient();
    private final CategoryDao categoryDao = new PostgresCategoryDao();
    private Article article;

    @BeforeAll
    void prepare() {
        article = ArticleCreationUtil.createArticle(RandomArticleGenerator.randomArticleWithAllFields());
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989496")
    @DisplayName("[T5] /article Успешное удаление статьи")
    void suckDelete() {
        client.delete(article.getId());
        assertArticleDelete(article.getId());
        assertCategoriesPersist(article.getCategories());
    }

    @Step("Не удаление категорий из справочника")
    private void assertCategoriesPersist(Set<Category> categories) {
        categories.forEach(c -> AssertUtil.assertEquals(c, categoryDao.getById(c.getId())));
    }

    @Step("Успешное удаление данных из таблицы article")
    private void assertArticleDelete(int id) {
        Callable<Record> artSupplier = () ->
                PostgresContext.getInstance().fetchOne(PropertyReader.getQuery("getArticleData"), id);
        Awaitility.await("Поиск статьи " + id).until(artSupplier, Matchers.nullValue());

        Callable<Result<Record>> catSupplier = () ->
                PostgresContext.getInstance().fetch(PropertyReader.getQuery("getArticleCategories"), id);
        Awaitility.await("Поиск категорий статьи " + id).until(catSupplier, Matchers.empty());
    }
}
