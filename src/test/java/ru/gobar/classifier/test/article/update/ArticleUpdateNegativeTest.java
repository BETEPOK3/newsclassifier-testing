package ru.gobar.classifier.test.article.update;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleUpdateClient;
import ru.gobar.classifier.api.request.ArticleCreateRequest;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.ArticleUtil;
import ru.gobar.classifier.util.RequestViolator;

import java.util.ArrayList;
import java.util.List;

import static ru.gobar.classifier.Endpoints.ARTICLE_UPDATE;
import static ru.gobar.classifier.data.Constants.WRONG_STRING;

@DisplayName(ARTICLE_UPDATE + " - обновление статьи")
public class ArticleUpdateNegativeTest extends AbstractTest {

    private final ArticleUpdateClient client = new ArticleUpdateClient();

    private int target;
    private final ArticleCreateRequest requestFull = ArticleCreateRequest.instance(
            RandomArticleGenerator.randomArticleWithAllFields());

    private int notExist;

    @BeforeAll
    void prepare() {
        notExist = Integer.MAX_VALUE;
        target = ArticleUtil.createArticle(RandomArticleGenerator.randomArticleWithAllFields()).getId();
    }

    private List<TestData> supplier() {
        return List.of(new TestData("Случайная строка", WRONG_STRING, HttpStatus.SC_BAD_REQUEST),
                new TestData("Пустая строка", "", HttpStatus.SC_TEMPORARY_REDIRECT),
                new TestData("Не существующий id", notExist, HttpStatus.SC_BAD_REQUEST),
                new TestData("Не существующий id", -1, HttpStatus.SC_BAD_REQUEST)
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989554")
    @DisplayName("[T29] /article/update Неуспешное получение статьи - некорректный параметр")
    void testBase() {
        AllureStepUtil stepper = new AllureStepUtil();
        supplier().forEach(data -> stepper.runStep(data.name, () -> client.get(data.params).assertThat().statusCode(data.status)));
        stepper.check();
    }

    private List<TestData> wrongSupplier() {
        return List.of(
                new TestData(requestFull, Article.Fields.article_title.name(), new ArrayList<>(), HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData(requestFull, Article.Fields.article_date.name(), WRONG_STRING, HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData(requestFull, Article.Fields.article_date.name(), new ArrayList<>(), HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData(requestFull, Article.Fields.article_text.name(), new ArrayList<>(), HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData(requestFull, Article.Fields.article_categories.name(), WRONG_STRING, HttpStatus.SC_UNPROCESSABLE_ENTITY), // TODO:
                new TestData(requestFull, Article.Fields.article_categories.name(), List.of(new ArrayList()), HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData(requestFull, Article.Fields.article_categories.name(), new ArrayList<>(), HttpStatus.SC_BAD_REQUEST), // TODO:
                new TestData(requestFull, Article.Fields.article_author.name(), new ArrayList<>(), HttpStatus.SC_UNPROCESSABLE_ENTITY)
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989557")
    @DisplayName("[T31] /article/update Неуспешное обновление статьи с некорректными параметрами")
    void wrongAttributes() {
        AllureStepUtil stepper = new AllureStepUtil();
        wrongSupplier().forEach(data -> stepper.runStep(data.keys, () -> client.post(RequestViolator.replace(data.keys, data.request, data.value), target).
                assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)));
        stepper.check();
    }

    private static class TestData {

        private String name;
        private Object params;
        private int status;

        private ArticleCreateRequest request;
        private String keys;
        private Object value;

        public TestData(String name, Object params, int status) {
            this.name = name;
            this.params = params;
            this.status = status;
        }

        public TestData(ArticleCreateRequest request, String keys) {
            this.request = request;
            this.keys = keys;
        }

        public TestData(ArticleCreateRequest request, String keys, Object value, int status) {
            this.request = request;
            this.keys = keys;
            this.value = value;
            this.status = status;
        }
    }

}
