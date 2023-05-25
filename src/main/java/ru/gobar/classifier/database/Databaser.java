package ru.gobar.classifier.database;

import org.jooq.DSLContext;
import org.jooq.Record;
import ru.gobar.classifier.PropertyReader;

public class Databaser {

    private static final DSLContext db = PostgresContext.getInstance();

    public static int getLastId() {
        Record record = PostgresContext.getInstance().fetchOne(PropertyReader.getQuery("getLastId"));
        if (record == null) {
            return 0;
        }

        return record.get("article_id", Integer.class);
    }

}
