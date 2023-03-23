package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SQLHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    @Override
    public void clear() {
        SQLHelper.query("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        }, e -> {
            throw new StorageException(e);
        });
    }

    @Override
    public void update(Resume r) {
        SQLHelper.query("UPDATE resume SET uuid = ?, full_name = ?", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        }, e -> {
            throw new StorageException(e);
        });
    }

    @Override
    public void save(Resume r) {
        SQLHelper.query("INSERT INTO resume (uuid, full_name) VALUES(?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        }, e -> {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(r.getUuid());
            } else {
                throw new StorageException(e);
            }
        });
    }

    @Override
    public Resume get(String uuid) {
        return SQLHelper.query("SELECT * FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        }, e -> {
            throw new StorageException(e);
        });
    }

    @Override
    public void delete(String uuid) {
        SQLHelper.query("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        }, e -> {
            throw new StorageException(e);
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SQLHelper.query("SELECT * FROM resume ORDER BY full_name, uuid ASC", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return list;
        }, e -> {
            throw new StorageException(e);
        });
    }

    @Override
    public int size() {
        Integer size = SQLHelper.query("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new StorageException("Expected to return size", null);
        }, e -> {
            throw new StorageException(e);
        });
        assert size != null;
        return size;
    }
}
