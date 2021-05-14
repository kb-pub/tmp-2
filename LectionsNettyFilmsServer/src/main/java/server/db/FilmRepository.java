package server.db;

import lombok.SneakyThrows;
import org.eclipse.collections.api.factory.Bags;
import server.domain.Actor;
import server.domain.Film;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class FilmRepository {
    @SneakyThrows
    public Collection<Film> findFlatAll(Connection conn) {
        var resultSet = conn.createStatement()
                .executeQuery("select id, title, duration from films ");
        var result = new ArrayList<Film>();
        while (resultSet.next()) {
            result.add(new Film(resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getShort("duration")));
        }
        return result;
    }

    @SneakyThrows
    public Optional<Film> findFlatById(Connection conn, long id) {
        var ps = conn.prepareStatement(
                "select id, title, duration from films where id = ?");
        ps.setLong(1, id);
        var rs = ps.executeQuery();
        if (rs.next()) {
            return Optional.of(new Film(
                    rs.getLong("id"), rs.getString("title"),
                    rs.getShort("duration")));
        } else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    public Optional<Film> findById(Connection conn, long id) {
        var ps = conn.prepareStatement(
                "select id, title, duration from films where id = ?");
        ps.setLong(1, id);
        var rs = ps.executeQuery();
        if (rs.next()) {
            var film = extractFlatFilm(rs);
            var actors = selectActors(conn, film.getId());
            film.setActors(actors);
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    private Film extractFlatFilm(ResultSet rs) {
        return new Film(
                rs.getLong("id"), rs.getString("title"),
                rs.getShort("duration"));
    }

    @SneakyThrows
    private Collection<Actor> selectActors(Connection conn, long filmId) {
        var result = new ArrayList<Actor>();
        var ps = conn.prepareStatement("select distinct id, name " +
                "from actors a join actors_films af on a.id = af.id_actor where id_film = ?");
        ps.setLong(1, filmId);
        var rs = ps.executeQuery();
        while (rs.next())
            result.add(extractFlatActor(rs));
        return result;
    }

    @SneakyThrows
    private Actor extractFlatActor(ResultSet rs) {
        return new Actor(rs.getLong("id"), rs.getString("name"));
    }

    @SneakyThrows
    public Film save(Connection conn, Film film) {
        if (film.getId() == 0) {
            return insert(conn, film);
        } else {
            return update(conn, film);
        }
    }

    private Film update(Connection conn, Film film) throws SQLException {
        var filmOld = findById(conn, film.getId())
                .orElseThrow(() -> new SQLException("no film with id " + film.getId()));

        updateFlatFilm(conn, film);

        var actorsToDelete = new HashSet<>(filmOld.getActors());
        actorsToDelete.removeAll(film.getActors());
        deleteActors(conn, film.getId(), actorsToDelete);

        var actorsToInsert = new HashSet<>(film.getActors());
        actorsToInsert.removeAll(filmOld.getActors());
        insertActors(conn, film.getId(), actorsToInsert);

        return film;
    }

    @SneakyThrows
    private void deleteActors(Connection conn, long filmId, Collection<Actor> actors) {
        if (!actors.isEmpty()) {
            var qMarks = String.join(",", Bags.immutable.withOccurrences("?", actors.size()));
            var ps = conn.prepareStatement("delete from actors_films " +
                    "where id_film = ? and id_actor in (" + qMarks + ")");
            int cnt = 1;
            ps.setLong(cnt++, filmId);
            for (var actor : actors)
                ps.setLong(cnt++, actor.getId());
            ps.executeUpdate();
        }
    }

    @SneakyThrows
    private void updateFlatFilm(Connection conn, Film film) {
        var ps = conn.prepareStatement("update films set title = ?, duration = ?" +
                " where id = ?");
        ps.setString(1, film.getTitle());
        ps.setShort(2, film.getDuration());
        ps.setLong(3, film.getId());
        ps.executeUpdate();
    }

    private Film insert(Connection conn, Film film) throws SQLException {
        var ps = conn.prepareStatement("insert into films (title, duration) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, film.getTitle());
        ps.setShort(2, film.getDuration());
        ps.executeUpdate();
        var keyRS = ps.getGeneratedKeys();
        if (!keyRS.next()) {
            throw new SQLException("cannot obtain auto generated key");
        }
        var filmId = keyRS.getLong(1);
        insertActors(conn, filmId, film.getActors());

        film.setId(filmId);
        return film;
    }

    private void insertActors(Connection conn, long filmId, Iterable<Actor> actors) throws SQLException {
        var ps = conn.prepareStatement("insert into actors_films(id_film, id_actor) values (?, ?)");
        actors.forEach(actor -> {
            try {
                ps.setLong(1, filmId);
                ps.setLong(2, actor.getId());
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        });
    }

    @SneakyThrows
    public Collection<Film> findAllLikeTitle(Connection conn, String titlePattern) {
        var ps = conn.prepareStatement(
                "select id, title, duration from films where lower(title) like ?");
        ps.setString(1, "%" + titlePattern.toLowerCase() + "%");
        var resultSet = ps.executeQuery();
        var result = new ArrayList<Film>();
        while (resultSet.next()) {
            result.add(new Film(resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getShort("duration")));
        }
        return result;
    }
}
