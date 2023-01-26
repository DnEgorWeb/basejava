package com.urise.webapp.storage;

import com.urise.webapp.exception.OverflowStorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    @Override
    public final void doSave(Resume r, Integer index) {
        if (size >= STORAGE_SIZE) {
            throw new OverflowStorageException(r.getUuid());
        } else {
            insertResume(r, index);
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
    public List<Resume> doGetAll() {
        return Arrays.asList(storage).subList(0, size);
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected void doUpdate(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    protected final void doDelete(Integer index) {
        deleteResume(index);
        size--;
    }

    @Override
    protected Boolean isExist(Integer index) {
        return index > -1;
    }

    @Override
    protected abstract Integer getSearchKey(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResume(int index);
}
