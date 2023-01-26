package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlternativeMapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> doGetAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.values()
                .stream()
                .filter(r -> r.equals(searchKey))
                .findFirst()
                .orElseThrow(() -> new NotExistStorageException(((Resume) searchKey).getUuid()));
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.values()
                .stream()
                .filter(r -> r.equals(searchKey))
                .findFirst()
                .ifPresent(r -> storage.remove(r.getUuid()));
    }

    @Override
    protected Boolean isExist(String uuid) {
        Resume r = storage.get(uuid);
        return r != null;
    }
}
