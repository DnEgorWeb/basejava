package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void doSave(Resume r, Integer index) {
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
    public final List<Resume> doGetAll() {
        return storage;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage.get(index);
    }

    @Override
    protected void doUpdate(Resume resume, Integer index) {
        storage.set(index, resume);
    }

    @Override
    protected final void doDelete(Integer index) {
        storage.remove(index.intValue());
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
    protected Boolean isExist(Integer index) {
        return index > -1;
    }
}
