package com.urise.webapp.storage;

import com.urise.webapp.exception.OverflowStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Nested
    @DisplayName("array save")
    class ArraySave {
        @Test
        @DisplayName("Throws overflow exception when no free space")
        void throwsOverflowWhenNoSpace() {
            try {
                for (int i = 0; i < AbstractArrayStorage.STORAGE_SIZE; i++) {
                    storage.save(new Resume());
                }
            } catch (OverflowStorageException e) {
                fail("Too early storage's overflow");
            }
            assertThrows(OverflowStorageException.class, () -> storage.save(new Resume()));
        }
    }
}