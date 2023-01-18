package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public final Resume get(String uuid) {
        return doGet(getExistingSearchKey(uuid));
    }

    @Override
    public void save(Resume r) {
        doSave(r, getNotExistingSearchKey(r.getUuid()));
    }

    @Override
    public final void update(Resume r) {
        doUpdate(r, getExistingSearchKey(r.getUuid()));
    }

    @Override
    public final void delete(String uuid) {
        doDelete(getExistingSearchKey(uuid));
    }

    private Object getExistingSearchKey(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private Object getNotExistingSearchKey(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index > -1) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract void doUpdate(Resume r, Object searchKey);

    protected abstract void doDelete(Object searchKey);
}
