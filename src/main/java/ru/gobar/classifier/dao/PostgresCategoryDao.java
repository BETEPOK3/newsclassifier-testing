package ru.gobar.classifier.dao;

import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.jooq.Record;
import ru.gobar.classifier.PropertyReader;
import ru.gobar.classifier.database.PostgresContext;
import ru.gobar.classifier.model.Category;

import java.util.concurrent.Callable;

public class PostgresCategoryDao implements CategoryDao{

    @Override
    public Category getById(int id) {
        Callable<Record> catSupplier = () ->
                PostgresContext.getInstance().fetchOne(PropertyReader.getQuery("getCategoryById"), id);
        Record record = Awaitility.await("Поиск категории " + id).until(catSupplier, Matchers.notNullValue());

        return new Category(id, record.get("category_name", String.class));
    }

    @Override
    public Category getByName(String name) {
        Callable<Record> catSupplier = () ->
                PostgresContext.getInstance().fetchOne(PropertyReader.getQuery("getCategoryByName"), name);
        Record record = Awaitility.await("Поиск категории " + name).until(catSupplier, Matchers.notNullValue());

        return new Category(record.get("category_id", Integer.class), name);
    }
}
