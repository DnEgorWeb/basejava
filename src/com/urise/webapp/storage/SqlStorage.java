package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SQLHelper sqlHelper;

    public SqlStorage(String url, String user, String password) {
        this.sqlHelper = new SQLHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() {
        sqlHelper.transactionalQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact")) {
                ps.execute();
            }
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
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
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
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.query("" + "    SELECT * FROM resume r " + " LEFT JOIN contact c " + "        ON r.uuid = c.resume_uuid " + "     WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String value = rs.getString("value");
                ContactType type = ContactType.valueOf(rs.getString("type"));
                r.addContact(type, value);
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
        return sqlHelper.query("SELECT * FROM resume ORDER BY full_name, uuid ASC", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return list;
        });
    }

    @Override
    public int size() {
        return sqlHelper.query("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }
}
