package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {
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

    protected SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract List<Resume> doGetAll();

    protected abstract Boolean isExist(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract void doUpdate(Resume r, SK searchKey);

    protected abstract void doDelete(SK searchKey);
}
