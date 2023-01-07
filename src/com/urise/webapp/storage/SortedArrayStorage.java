package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected void insertResume(Resume resume, int index) {
        int insertIndex = -index - 1;
        for (int i = size; i > insertIndex; i--) {
            storage[i] = storage[i - 1];
        }
        storage[insertIndex] = resume;
    }

    @Override
    public void deleteResume(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
        storage[size - 1] = null;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
