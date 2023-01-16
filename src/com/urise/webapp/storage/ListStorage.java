package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private final ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void save(Resume r, int index) {
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
    public final Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    protected Resume get(int index) {
        return storage.get(index);
    }

    @Override
    protected void update(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected final void delete(int index) {
        storage.remove(index);
    }

    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }
}
