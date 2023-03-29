package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SQLHelper sqlHelper;

    public SqlStorage(String url, String user, String password) {
        this.sqlHelper = new SQLHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() {
        sqlHelper.query("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            sqlHelper.query("DELETE FROM contact WHERE resume_uuid = ?", ps -> {
                ps.setString(1, r.getUuid());
                ps.execute();
                return null;
            });
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.query("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContact(r, rs);
            } while (rs.next());

            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.query("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.query("SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY r.full_name, r.uuid ASC", ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r = map.get(uuid);
                if (r == null) {
                    r = new Resume(uuid, rs.getString("full_name"));
                    map.put(r.getUuid(), r);
                }
                addContact(r, rs);
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.query("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void addContact(Resume r, ResultSet rs) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        r.addContact(type, value);
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
