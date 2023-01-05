package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_SIZE = 10000;
    protected final Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            showWarning("get", "resume with uuid " + uuid + " not found in the storage");
            return null;
        }
        return storage[index];
    }

    public int size() {
        return size;
    }

    protected void showWarning(String method, String warning) {
        System.out.printf("ArrayStorage warning: unable to perform %s operation. Reason: %s\n", method, warning);
    }

    protected abstract int getIndex(String uuid);
}
