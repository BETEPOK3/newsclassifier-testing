package ru.gobar.classifier.dao;

import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.jooq.Record;
import org.jooq.Result;
import ru.gobar.classifier.PropertyReader;
import ru.gobar.classifier.database.PostgresContext;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PostgresArticleDao implements ArticleDao {

    @Override
    @SneakyThrows
    public Article getById(int id) {
        Article article = new Article();
        Callable<Record> artSupplier = () ->
                PostgresContext.getInstance().fetchOne(PropertyReader.getQuery("getArticleData"), id);
        Record record = Awaitility.await("Поиск статьи " + id).until(artSupplier, Matchers.notNullValue());
        article.setId(id)
                .setTitle(record.get("article_title", String.class))
                .setAuthor(record.get("article_author", String.class))
                .setKeywords(Arrays.stream(record.get("article_keywords", String[].class)).collect(Collectors.toSet()))
                .setDate(record.get("article_date", LocalDate.class))
                .setText(record.get("article_text", String.class));

        Set<Category> categories = new HashSet<>();
        Result<Record> records = PostgresContext.getInstance().fetch(PropertyReader.getQuery("getArticleCategories"), id);
        if (records.isNotEmpty()) {
            categories.addAll(records.stream().map(r ->
                    new Category().setId(r.get("category_id", Integer.class)).
                            setName(r.get("category_name", String.class))).collect(Collectors.toSet()));
        }

        return article.setCategories(categories);
    }
}
