package ru.gobar.classifier.database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.postgresql.ds.PGSimpleDataSource;

import static ru.gobar.classifier.Endpoints.*;
import static ru.gobar.classifier.Endpoints.DB_NAME;

public class PostgresContext {

    private static final DSLContext db;

    static {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(DB_HOST);
        dataSource.setUser(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setDatabaseName(DB_NAME);
        DataSourceConnectionProvider provider = new DataSourceConnectionProvider(dataSource);
        db = DSL.using(provider,
                SQLDialect.POSTGRES,
                new Settings().withRenderFormatted(true));
    }

    public static DSLContext getInstance() {
        return db;
    }
}
