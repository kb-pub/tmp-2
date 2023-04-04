package library.lv5.impl.repo.pg;

import library.lv2.spi.repo.BaseRepository;
import library.lv2.spi.repo.RepositoryAppException;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class PgBaseRepository implements BaseRepository {
    protected final PgDataSource dataSource;

    @Override
    public void beginTransaction() {
        try {
            //dataSource.getConnection().setTransactionIsolation();
            dataSource.getConnection().setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void commitTransaction() {
        try {
            dataSource.getConnection().commit();
            dataSource.getConnection().setAutoCommit(true);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            dataSource.getConnection().rollback();
            dataSource.getConnection().setAutoCommit(true);
        }
        catch (SQLException e) {
            throw new RepositoryAppException(e.getMessage());
        }
    }
}
