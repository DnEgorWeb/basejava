package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_SIZE = 10000;
    private final Resume[] storage = new Resume[STORAGE_SIZE];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (hasResume(r.getUuid())) {
            showWarning("save", "resume with uuid " + r.getUuid() + " already exists in the storage");
        } else if (size >= STORAGE_SIZE) {
            showWarning("save", "no free space in the storage");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index == -1) {
            showWarning("update", "resume with uuid " + resume.getUuid() + " not found in the storage");
            return;
        }
        storage[index] = resume;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            showWarning("get", "resume with uuid " + uuid + " not found in the storage");
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            showWarning("delete", "resume with uuid " + uuid + " not found in the storage");
            return;
        }
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private void showWarning(String method, String warning) {
        System.out.printf("ArrayStorage warning: unable to perform %s operation. Reason: %s\n", method, warning);
    }

    private boolean hasResume(String uuid) {
        return getIndex(uuid) != -1;
    }

    /**
     * @return positive integer indicating index of desired resume if resume is found. Return -1 otherwise.
     */
    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
