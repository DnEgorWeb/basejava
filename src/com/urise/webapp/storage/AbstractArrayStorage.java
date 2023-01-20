package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.OverflowStorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

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
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
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
    protected Object getNotExistingSearchKey(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index > -1) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    @Override
    protected Object getExistingSearchKey(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    @Override
    protected abstract Object getSearchKey(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResume(int index);
}
