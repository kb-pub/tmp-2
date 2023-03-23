package library.lv5.impl.repo;

import library.lv0.crosscutting.Settings;
import library.lv1.entity.Book;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.RepositoryAppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PgBookRepository implements BookRepository {
    private Connection connection = null;

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        Settings.PG_CONN_STRING,
                        Settings.PG_USERNAME,
                        Settings.PG_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            var conn = getConnection();
            try (var stmt = conn.prepareStatement("select * from books")) {
                var result = new ArrayList<Book>();
                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(new Book(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getInt("year")));
                    }
                }
                return Collections.unmodifiableList(result);
            }
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public Book save(Book book) {
        return book.getId() == 0 ? insert(book) : update(book);
    }

    private Book insert(Book book) {
        try {
            var conn = getConnection();
            try (var stmt = conn.prepareStatement("""
                    insert into books (id, title, year)
                    values (default, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, book.getTitle());
                stmt.setInt(2, book.getYear());
                ResultSet keyRs;
                stmt.executeUpdate();
                keyRs = stmt.getGeneratedKeys();
                if (!keyRs.next())
                    throw new RepositoryAppException("no generated keys in query result!");
                var id = keyRs.getLong(1);
                return book.toBuilder().id(id).build();
            }
        } catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    private Book update(Book book) {
//        try {
//            var conn = getConnection();
//            try (var stmt = connection.prepareStatement()) {
//
//            }
//        } catch (SQLException e) {
//            throw new RepositoryAppException(e.getMessage());
//        }
        return null;
    }
}
