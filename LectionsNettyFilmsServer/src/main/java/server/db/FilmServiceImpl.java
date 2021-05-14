package server.db;

import lombok.SneakyThrows;
import server.domain.Film;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class FilmServiceImpl implements FilmService {
    public static final int SERIALIZATION_ANOMALY_ATTEMPTS = 5;
    public static final int SERIALIZATION_ANOMALY_ERROR_CODE = 40001;

    private final DataSource src;
    private final FilmRepository filmRepository;

    public FilmServiceImpl(DataSource src, FilmRepository filmRepository) {
        this.src = src;
        this.filmRepository = filmRepository;
    }

    @Override
    @SneakyThrows
    public Collection<Film> findFlatAll() {
        try (var conn = src.getConnection()) {
            return filmRepository.findFlatAll(conn);
        }
    }

    @SneakyThrows
    public Optional<Film> findFlatById(long id) {
        try (var conn = src.getConnection()) {
            return filmRepository.findFlatById(conn, id);
        }
    }

    @SneakyThrows
    public Optional<Film> findById(long id) {
        try (var conn = src.getConnection()) {
            return filmRepository.findById(conn, id);
        }
    }

    @Override
    @SneakyThrows
    public Film save(Film film) {
        try (var conn = src.getConnection()) {
            var attempts = SERIALIZATION_ANOMALY_ATTEMPTS;
            var idOrigin = film.getId();
            while (attempts-- > 0) {
                film.setId(idOrigin);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                conn.setAutoCommit(false);
                try {
                    film = filmRepository.save(conn, film);
                    conn.commit();
                    return film;
                } catch (SQLException e) {
                    System.out.println("error " + e);
                    conn.rollback();
                    if (e.getErrorCode() != SERIALIZATION_ANOMALY_ERROR_CODE) {
                        throw e;
                    }
                }
            }
            throw new SQLException("too many transaction serialization errors");
        }
    }

    @Override
    @SneakyThrows
    public Collection<Film> findFlatAllLikeTitle(String titlePattern) {
        try (var conn = src.getConnection()) {
            return filmRepository.findAllLikeTitle(conn, titlePattern);
        }
    }
}
