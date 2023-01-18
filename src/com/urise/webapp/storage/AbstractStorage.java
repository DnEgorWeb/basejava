package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public final Resume get(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return get(index);
    }

    @Override
    public void save(Resume r) {
        int index = (int) getSearchKey(r.getUuid());
        if (index > -1) {
            throw new ExistStorageException(r.getUuid());
        }
        save(r, index);
    }

    @Override
    public final void update(Resume resume) {
        int index = (int) getSearchKey(resume.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        update(resume, index);
    }

    @Override
    public final void delete(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        delete(index);
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume get(int index);

    protected abstract void save(Resume r, int index);

    protected abstract void update(Resume r, int index);

    protected abstract void delete(int index);
}
