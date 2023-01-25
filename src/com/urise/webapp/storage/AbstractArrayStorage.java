package com.urise.webapp.storage;

import com.urise.webapp.exception.OverflowStorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    @Override
    public final void doSave(Resume r, Object searchKey) {
        if (size >= STORAGE_SIZE) {
            throw new OverflowStorageException(r.getUuid());
        } else {
            insertResume(r, (int) searchKey);
            size++;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        return Arrays.asList(storage).subList(0, size);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected final void doDelete(Object searchKey) {
        deleteResume((int) searchKey);
        size--;
    }

    @Override
    protected Boolean isExist(String uuid) {
        int index = getSearchKey(uuid);
        return index > -1;
    }

    @Override
    protected abstract Integer getSearchKey(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResume(int index);
}
