package ru.gobar.classifier.database;

import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.jooq.DSLContext;
import org.jooq.Record;
import ru.gobar.classifier.PropertyReader;
import ru.gobar.classifier.model.Category;

import java.util.concurrent.Callable;

public class Databaser {

    private static final DSLContext db = PostgresContext.getInstance();

    public static int getLastId() {
        Record record = db.fetchOne(PropertyReader.getQuery("getLastId"));
        if (record == null) {
            return 0;
        }

        return record.get("article_id", Integer.class);
    }

    public static Category getCategoryById(int id) {
        Callable<Record> catSupplier = () ->
                db.fetchOne(PropertyReader.getQuery("getCategoryById"), id);
        Record record = Awaitility.await("Поиск категории " + id).until(catSupplier, Matchers.notNullValue());

        return new Category(id, record.get("category_name", String.class));
    }

}
