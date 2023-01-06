package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            showWarning("get", "resume with uuid " + uuid + " not found in the storage");
            return null;
        }
        return storage[index];
    }

    public final void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > -1) {
            showWarning("save", "resume with uuid " + r.getUuid() + " already exists in the storage");
        } else if (size >= STORAGE_SIZE) {
            showWarning("save", "no free space in the storage");
        } else {
            insert(r, index);
            size++;
        }
    }

    public final void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            showWarning("update", "resume with uuid " + resume.getUuid() + " not found in the storage");
            return;
        }
        storage[index] = resume;
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            showWarning("delete", "resume with uuid " + uuid + " not found in the storage");
            return;
        }
        delete(index);
        size--;
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected void showWarning(String method, String warning) {
        System.out.printf("ArrayStorage warning: unable to perform %s operation. Reason: %s\n", method, warning);
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insert(Resume resume, int index);

    protected abstract void delete(int index);
}
