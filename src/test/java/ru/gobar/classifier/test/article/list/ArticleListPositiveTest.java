package ru.gobar.classifier.test.article.list;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleListClient;
import ru.gobar.classifier.api.response.ArticleGetResponse;
import ru.gobar.classifier.api.response.ArticleListResponse;
import ru.gobar.classifier.dao.CategoryDao;
import ru.gobar.classifier.dao.PostgresCategoryDao;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.database.Databaser;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.ArticleUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_INDEX;
import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_ROOT;

@DisplayName(ARTICLE_LIST_ROOT + " и " + ARTICLE_LIST_INDEX + " - получение списка статей")
public class ArticleListPositiveTest extends AbstractTest {

    private final int PAGE_SIZE = 10;
    private final int TOTAL = 12;

    private final ArticleListClient clientRoot = new ArticleListClient(ARTICLE_LIST_ROOT);
    private final ArticleListClient clientIndex = new ArticleListClient(ARTICLE_LIST_INDEX);
    private final CategoryDao categoryDao = new PostgresCategoryDao();

    private Category cat = new Category().setName(Category.RealCats.ANIME.getName()).setId(Category.RealCats.ANIME.getId());
    private Category testCat = new Category().setName(Category.RealCats.TEST_CAT.getName()).setId(Category.RealCats.TEST_CAT.getId());
    private List<ArticleListClient> clients() {
        return List.of(clientRoot, clientIndex);
    }

    private Map<String, Comparator<ArticleGetResponse>> sorts() {
        return Map.of(ArticleListClient.SortBy.date.name(), Comparator.comparing(ArticleGetResponse::getDate).reversed(),
                ArticleListClient.SortBy.title.name(), Comparator.comparing(ArticleGetResponse::getTitle),
                ArticleListClient.SortBy.author.name(), new ArticleGetResponse.AuthorGetResponseComparator());
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989489")
    @DisplayName("[T10] Получение статей с фильтрацией")
    void sortTest() {
        List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                RandomArticleGenerator.randomArticleWithAllFields(),
                RandomArticleGenerator.randomArticleWithAllFields()));

        articles.forEach(a -> {
            a.getCategories().add(cat);
            ArticleUtil.createArticle(a);
        });

        AllureStepUtil outer = new AllureStepUtil();
        clients().forEach(c -> outer.runStep(c.ENDPOINT, () -> {
            AllureStepUtil s = new AllureStepUtil();
            sorts().forEach((k, v) -> s.runStep(k, () -> {
                ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, cat.getId(),
                                ArticleListClient.SORT_PARAM, k)).
                        assertThat().statusCode(200).extract().as(ArticleListResponse.class);

                for (ArticleGetResponse a : actual.getArticles()) {
                    assertThat(a.getCategories().stream().map(ArticleGetResponse.Category::getId).collect(Collectors.toList()),
                            Matchers.hasItem(cat.getId()));
                }

                List<ArticleGetResponse> expectedOrder = actual.getArticles().stream().sorted(v).collect(Collectors.toList());
                for (int i = 0; i < expectedOrder.size(); ++i) {
                    AssertUtil.assertEquals(expectedOrder.get(i).getId(), actual.getArticles().get(i).getId());
                }
            }));
            s.check();
        }));
        outer.check();
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989487")
    @DisplayName("[T11] Получение статей по категории")
    void catTest() {
        List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                RandomArticleGenerator.randomArticleWithAllFields()));

        articles.forEach(a -> {
            a.getCategories().add(cat);
            ArticleUtil.createArticle(a);
        });

        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, cat.getId())).
                    assertThat().statusCode(200).extract().as(ArticleListResponse.class);

            for (ArticleGetResponse a : actual.getArticles()) {
                assertThat(a.getCategories().stream().map(ArticleGetResponse.Category::getId).collect(Collectors.toList()),
                        Matchers.hasItem(cat.getId()));
            }
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989488")
    @DisplayName("[T12] Получение статей по всем категориям")
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
    @TmsLink("https://www.hostedredmine.com/attachments/989504")
    @DisplayName("[T13] Получение статей по совпадению в названии")
    void titleTextTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            String inner = UUID.randomUUID().toString();
            List<Article> articles = new java.util.ArrayList<>(List.of(RandomArticleGenerator.randomArticleBase(),
                    RandomArticleGenerator.randomArticleWithAllFields()));

            articles.forEach(a -> {
                a.setTitle(UUID.randomUUID() + inner + UUID.randomUUID());
                ArticleUtil.createArticle(a);
            });
            ArticleListResponse expected = ArticleListResponse.instance(articles);

            ArticleListResponse actual = c.get(Map.of(ArticleListClient.TEXT_PARAM, inner)).
                    assertThat().statusCode(200).extract().as(ArticleListResponse.class);

            AssertUtil.assertEquals(expected, actual);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989503")
    @DisplayName("[T14] Получение статей по страницам")
    void pageTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            int i = 0;
            do {
                ArticleListResponse actual = c.get(Map.of(ArticleListClient.CATEGORY_PARAM, testCat.getId(),
                                ArticleListClient.PAGE_PARAM, i,
                                ArticleListClient.SORT_PARAM, ArticleListClient.SortBy.title.name())).
                        assertThat().statusCode(200).extract().as(ArticleListResponse.class);

                for (ArticleGetResponse a : actual.getArticles()) {
                    assertThat(a.getCategories().stream().map(ArticleGetResponse.Category::getId).collect(Collectors.toList()),
                            Matchers.hasItem(testCat.getId()));
                }
                List<ArticleGetResponse> expectedOrder = actual.getArticles().stream()
                        .sorted(Comparator.comparing(ArticleGetResponse::getTitle))
                        .collect(Collectors.toList());
                for (int j = 0; j < expectedOrder.size(); ++j) {
                    AssertUtil.assertEquals(expectedOrder.get(j).getId(), actual.getArticles().get(j).getId());
                }
                ++i;
            } while ((i + 1) * PAGE_SIZE <= TOTAL + PAGE_SIZE);
        }));
        stepUtil.check();
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989502")
    @DisplayName("[T15] Получение статей без параметров")
    void nonParamsTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> c.get(new HashMap<>()).
                assertThat().statusCode(HttpStatus.SC_OK)));
        stepUtil.check();
    }

}
