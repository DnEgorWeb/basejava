package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.query("UPDATE resume SET uuid = ?, full_name = ?", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.query("INSERT INTO resume (uuid, full_name) VALUES(?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.query("SELECT * FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
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
        Integer size = sqlHelper.query("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new StorageException("Expected to return size", null);
        });
        assert size != null;
        return size;
    }
}
