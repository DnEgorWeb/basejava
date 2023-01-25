package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void doSave(Resume r, Object searchKey) {
        storage.add(r);
    }

    @Override
    public final int size() {
        return storage.size();
    }

    @Override
    public final void clear() {
        storage.clear();
    }

    @Override
    public final List<Resume> getAllSorted() {
        return storage;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected final void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected Boolean isExist(String uuid) {
        int index = getSearchKey(uuid);
        return index > -1;
    }
}
