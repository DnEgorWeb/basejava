package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLHelper {
    private final ConnectionFactory connectionFactory;

    public SQLHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T query(String statement, Supplier<T> cb) {
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(statement)) {
            return cb.call(ps);
        } catch (SQLException e) {
            throw getException(e);
        }
    }

    public <T> T transactionalQuery(Transaction<T> cb) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = cb.call(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw getException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T call(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface Transaction<T> {
        T call(Connection conn) throws SQLException;
    }

    private StorageException getException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new ExistStorageException(null);
        } else {
            return new StorageException(e);
        }
    }
}
