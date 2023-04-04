package library.lv5.impl.repo.pg;

import library.lv0.crosscutting.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDataSource {
    private Connection connection = null;

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    Settings.PG_CONN_STRING,
                    Settings.PG_USERNAME,
                    Settings.PG_PASSWORD);
        }
        return connection;
    }
}
