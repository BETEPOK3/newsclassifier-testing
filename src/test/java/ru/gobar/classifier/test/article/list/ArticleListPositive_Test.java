package ru.gobar.classifier.test.article.list;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleListClient;
import ru.gobar.classifier.api.response.ArticleListResponse;
import ru.gobar.classifier.dao.CategoryDao;
import ru.gobar.classifier.dao.PostgresCategoryDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.data.RandomCategoryGenerator;
import ru.gobar.classifier.database.Databaser;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.ArticleCreationUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.util.*;

import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_INDEX;
import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_ROOT;

@DisplayName("get article list")
public class ArticleListPositive_Test extends AbstractTest {

    private final int PAGE_SIZE = 2;

    private final ArticleListClient clientRoot = new ArticleListClient(ARTICLE_LIST_ROOT);
    private final ArticleListClient clientIndex = new ArticleListClient(ARTICLE_LIST_INDEX);
    private final CategoryDao categoryDao = new PostgresCategoryDao();

    private List<ArticleListClient> clients() {
        return List.of(clientRoot, clientIndex);
    }

    private Map<String, Comparator<Article>> sorts() {
        return Map.of(ArticleListClient.SortBy.date.name(), Comparator.comparing(Article::getDate),
                ArticleListClient.SortBy.title.name(), Comparator.comparing(Article::getTitle),
                ArticleListClient.SortBy.author.name(), new Article.AuthorComparator());
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей с фильтрацией")
    void sortTest() {
        AllureStepUtil outer = new AllureStepUtil();
        clients().forEach(c -> outer.runStep(c.ENDPOINT, () -> {
            AllureStepUtil s = new AllureStepUtil();
            sorts().forEach((k, v) -> s.runStep(k, () -> {
                Category cat = categoryDao.persist(RandomCategoryGenerator.randomCategory());
                List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                        RandomArticleGenerator.randomArticleWithAllFields(),
                        RandomArticleGenerator.randomArticleWithAllFields()));

                articles.forEach(a -> {
                    a.getCategories().add(cat);
                    ArticleCreationUtil.createArticle(a);
                });
                ArticleListResponse expected = ArticleListResponse.instance(articles);
                articles.sort(v);

                ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, cat.getId(),
                                ArticleListClient.SORT_PARAM, k)).
                        assertThat().statusCode(200).extract().as(ArticleListResponse.class);

                AssertUtil.assertEquals(expected, actual);

                for (int i = 0; i < articles.size(); ++i) {
                    AssertUtil.assertEquals(articles.get(i).getId(), actual.getArticles().get(i).getId());
                }
            }));
            s.check();
        }));
        outer.check();
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей по категории")
    void catTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            Category cat = categoryDao.persist(RandomCategoryGenerator.randomCategory());
            List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                    RandomArticleGenerator.randomArticleWithAllFields()));

            articles.forEach(a -> {
                a.getCategories().add(cat);
                ArticleCreationUtil.createArticle(a);
            });
            ArticleListResponse expected = ArticleListResponse.instance(articles);

            ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, cat.getId())).
                    assertThat().statusCode(200).extract().as(ArticleListResponse.class);

            AssertUtil.assertEquals(expected, actual);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей по всем категориям")
    void allCatTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            int actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, -1,
                            ArticleListClient.SORT_PARAM, ArticleListClient.SortBy.title.name())).
                    assertThat().statusCode(200).extract().path("articles_count");

            AssertUtil.assertEquals(Databaser.getArticleCount(), actual);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей по совпадению в названии")
    void titleTextTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            String inner = UUID.randomUUID().toString();
            List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                    RandomArticleGenerator.randomArticleWithAllFields()));

            articles.forEach(a -> {
                a.setTitle(UUID.randomUUID() + inner + UUID.randomUUID());
                ArticleCreationUtil.createArticle(a);
            });
            ArticleListResponse expected = ArticleListResponse.instance(articles);

            ArticleListResponse actual = c.get(Map.of(ArticleListClient.TEXT_PARAM, inner)).
                    assertThat().statusCode(200).extract().as(ArticleListResponse.class);

            AssertUtil.assertEquals(expected, actual);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей по страницам")
    void pageTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            Category cat = categoryDao.persist(RandomCategoryGenerator.randomCategory());
            List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                    RandomArticleGenerator.randomArticleWithAllFields(),
                    RandomArticleGenerator.randomArticleWithAllFields()));

            articles.forEach(a -> {
                a.getCategories().add(cat);
                ArticleCreationUtil.createArticle(a);
            });
            articles.sort(Comparator.comparing(Article::getTitle));

            int i = 0;
            do {
                List<Article> a = new ArrayList<>();
                try {
                    a = articles.subList(i * PAGE_SIZE, (i + 1) * PAGE_SIZE);
                } catch (IndexOutOfBoundsException e) {
                    try {
                        a = articles.subList(i * PAGE_SIZE, articles.size());
                    } catch (IndexOutOfBoundsException ignored) {

                    }
                }

                ArticleListResponse expected = ArticleListResponse.instance(a);
                expected.setCount(articles.size());
                ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, cat.getId(),
                                ArticleListClient.PAGE_SIZE_PARAM, PAGE_SIZE,
                                ArticleListClient.PAGE_PARAM, i,
                                ArticleListClient.SORT_PARAM, ArticleListClient.SortBy.title.name())).
                        assertThat().statusCode(200).extract().as(ArticleListResponse.class);

                AssertUtil.assertEquals(expected, actual);
                ++i;
            } while ((i + 1) * PAGE_SIZE > articles.size() + PAGE_SIZE);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("...")
    @DisplayName("Получение статей по страницам")
    void nonParamsTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> c.get(new HashMap<>()).
                assertThat().statusCode(HttpStatus.SC_OK)));
        stepUtil.check();
    }

}
