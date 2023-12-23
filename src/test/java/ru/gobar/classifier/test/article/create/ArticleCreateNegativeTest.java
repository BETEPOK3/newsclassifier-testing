package ru.gobar.classifier.test.article.create;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleCreateClient;
import ru.gobar.classifier.api.request.ArticleCreateRequest;
import ru.gobar.classifier.data.RandomArticleGenerator;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.RequestViolator;

import java.util.ArrayList;
import java.util.List;

import static ru.gobar.classifier.Endpoints.ARTICLE_CREATE;
import static ru.gobar.classifier.data.Constants.WRONG_STRING;

@DisplayName(ARTICLE_CREATE + " - создание статей")
public class ArticleCreateNegativeTest extends AbstractTest {

    private final ArticleCreateRequest requestFull = ArticleCreateRequest.instance(
            RandomArticleGenerator.randomArticleWithAllFields());
    private final ArticleCreateClient client = new ArticleCreateClient();

    private List<TestData> missSupplier() {
        return List.of(new TestData(requestFull, Article.Fields.article_title.name()),
                new TestData(requestFull, Article.Fields.article_text.name()),
                new TestData(requestFull, Article.Fields.article_categories.name()));
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989189")
    @DisplayName("[T1] /article/create Неуспешное создание статьи с отсутствующими обязательными параметрами")
    void missAttributes() {
        AllureStepUtil stepper = new AllureStepUtil();
        missSupplier().forEach(data -> stepper.runStep(data.keys, () -> client.post(RequestViolator.remove(data.keys, data.request)).
                assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)));
        stepper.check();
    }

    private List<TestData> wrongSupplier() {
        return List.of(new TestData(requestFull, Article.Fields.article_title.name(), null),
                new TestData(requestFull, Article.Fields.article_title.name(), new ArrayList<>()),
                new TestData(requestFull, Article.Fields.article_date.name(), WRONG_STRING),
                new TestData(requestFull, Article.Fields.article_date.name(), new ArrayList<>()),
                new TestData(requestFull, Article.Fields.article_text.name(), null),
                new TestData(requestFull, Article.Fields.article_text.name(), new ArrayList<>()),
                new TestData(requestFull, Article.Fields.article_categories.name(), null),
                new TestData(requestFull, Article.Fields.article_categories.name(), WRONG_STRING),
                new TestData(requestFull, Article.Fields.article_categories.name(), List.of(new ArrayList())),
                new TestData(requestFull, Article.Fields.article_categories.name(), new ArrayList<>()),
                new TestData(requestFull, Article.Fields.article_author.name(), new ArrayList<>()),
                new TestData(requestFull, Article.Fields.article_keywords.name(), WRONG_STRING)
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989188")
    @DisplayName("[T2] /article/create Неуспешное создание статьи с некорректными параметрами")
    void wrongAttributes() {
        AllureStepUtil stepper = new AllureStepUtil();
        wrongSupplier().forEach(data -> stepper.runStep(data.keys, () -> client.post(RequestViolator.replace(data.keys, data.request, data.value)).
                assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)));
        stepper.check();
    }

    private static class TestData {

        private final ArticleCreateRequest request;
        private final String keys;
        private Object value;

        public TestData(ArticleCreateRequest request, String keys) {
            this.request = request;
            this.keys = keys;
        }

        public TestData(ArticleCreateRequest request, String keys, Object value) {
            this.request = request;
            this.keys = keys;
            this.value = value;
        }
    }
}
