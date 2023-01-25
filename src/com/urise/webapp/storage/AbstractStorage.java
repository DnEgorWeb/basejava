package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    private final Comparator<Resume> fullNameComparator = Comparator.comparing(Resume::getFullName);
    private final Comparator<Resume> uuidComparator = Comparator.comparing(Resume::getUuid);
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

    @Override
    public final List<Resume> getAllSorted() {
        List<Resume> resumes = doGetAll();
        resumes.sort(fullNameComparator.thenComparing(uuidComparator));
        return resumes;
    }

    protected Object getNotExistingSearchKey(String uuid) {
        if (isExist(uuid)) {
            throw new ExistStorageException(uuid);
        }
        return getSearchKey(uuid);
    }

    protected Object getExistingSearchKey(String uuid) {
        if (!isExist(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        return getSearchKey(uuid);
    }

    protected abstract List<Resume> doGetAll();

    protected abstract Boolean isExist(String uuid);

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract void doUpdate(Resume r, Object searchKey);

    protected abstract void doDelete(Object searchKey);
}
