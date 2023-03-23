package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SqlStorageTest extends AbstractStorageTest {
    public final ConnectionFactory connectionFactory;

    protected SqlStorageTest() {
        super(new SqlStorage());
        connectionFactory = () -> DriverManager.getConnection(Config.get().getDbUrl(), Config.get().getDbUsername(), Config.get().getDbPassword());
        try (Connection conn = connectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(Config.get().getLaunchSqlScript());
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
