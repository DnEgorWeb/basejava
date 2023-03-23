package com.urise.webapp.sql;

import com.urise.webapp.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SQLHelper {
    private static final ConnectionFactory connectionFactory = () -> DriverManager.getConnection(Config.get().getDbUrl(), Config.get().getDbUsername(), Config.get().getDbPassword());

    public static <T> T query(String statement, Supplier<T> cb, Consumer<SQLException> errHandler) {
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(statement)) {
            return cb.call(ps);
        } catch (SQLException e) {
            errHandler.accept(e);
        }
        return null;
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T call(PreparedStatement ps) throws SQLException;
    }
}
