package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.OverflowStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class AbstractArrayStorageTest {
    private Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @AfterEach
    public void cleanUp() throws Exception {
        Constructor<? extends Storage> constructor = storage.getClass().getConstructor();
        storage = constructor.newInstance();
    }

    @Nested
    @DisplayName("get")
    class Get {
        @Test
        @DisplayName("Throws not exists when no matching resume")
        void throwsNotExistsWhenNoMatchingResume() {
            assertThrows(NotExistStorageException.class, () -> storage.get(UUID.randomUUID().toString()));
        }

        @Test
        @DisplayName("Returns resume")
        void returnsResume() {
            Resume r = new Resume();

            storage.save(r);

            assertEquals(r, storage.get(r.getUuid()));
        }
    }

    @Nested
    @DisplayName("save")
    class Save {
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

        @Test
        @DisplayName("Throws exists exception when already has resume")
        void throwsResumeExistsWhenHasSuchResume() {
            Resume r = new Resume();

            storage.save(r);

            assertThrows(ExistStorageException.class, () -> storage.save(r));
        }

        @Test
        @DisplayName("Saves resume to the storage")
        void savesResumeToStorage() {
            Resume r = new Resume();

            storage.save(r);

            assertEquals(1, storage.size());
            assertEquals(r, storage.get(r.getUuid()));
        }
    }

    @Nested
    @DisplayName("update")
    class Update {
        @Test
        @DisplayName("Throws not exists when no matching resume")
        void throwsNotExistsWhenNoMatchingResume() {
            assertThrows(NotExistStorageException.class, () -> storage.update(new Resume()));
        }

        @Test
        @DisplayName("Updates resume")
        void updatesResume() {
            Resume r = new Resume();
            storage.save(r);
            assumeTrue(storage.size() == 1);
            assumeTrue(r.equals(storage.get(r.getUuid())));

            Resume r1 = new Resume(r.getUuid());
            storage.update(r1);

            assertEquals(1, storage.size());
            assertEquals(r1, storage.get(r.getUuid()));
            assertNotSame(r, storage.get(r.getUuid()));
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {
        @Test
        @DisplayName("Throws not exists when no matching resume")
        void throwsNotExistsWhenNoMatchingResume() {
            assertThrows(NotExistStorageException.class, () -> storage.delete(UUID.randomUUID().toString()));
        }

        @Test
        @DisplayName("Deletes resume")
        void deletesResume() {
            Resume r = new Resume();
            storage.save(r);
            assumeTrue(storage.size() == 1);

            storage.delete(r.getUuid());

            assertEquals(0, storage.size());
            assertThrows(NotExistStorageException.class, () -> storage.get(r.getUuid()));
        }
    }

    @Nested
    @DisplayName("size")
    class Size {
        @Test
        @DisplayName("Returns 0 when empty")
        void returnsZeroWhenEmpty() {
            assertEquals(0, storage.size());
        }

        @Test
        @DisplayName("Returns value equal to amount of saved resumes")
        void returnsValueEqualToAmountOfSavedResumes() {
            storage.save(new Resume());
            storage.save(new Resume());
            storage.save(new Resume());

            assertEquals(3, storage.size());
        }
    }

    @Nested
    @DisplayName("clear")
    class Clear {
        @Test
        @DisplayName("Clears storage")
        void clearsStorage() {
            storage.save(new Resume());
            storage.save(new Resume());
            storage.save(new Resume());
            assumeTrue(storage.size() == 3);

            storage.clear();

            assertEquals(0, storage.size());
        }
    }

    @Nested
    @DisplayName("getAll")
    class GetAll {
        @Test
        @DisplayName("Returns empty array when no resumes in storage")
        void returnsEmptyArrayWhenNoResumes() {
            assertEquals(0, storage.getAll().length);
        }

        @Test
        @DisplayName("Returns all resumes")
        void returnsAllResumes() {
            Resume r1 = new Resume();
            Resume r2 = new Resume();

            storage.save(r1);
            storage.save(r2);

            Resume[] resumes = storage.getAll();
            assertEquals(2, resumes.length);
            assertEquals(r1, storage.get(r1.getUuid()));
            assertEquals(r2, storage.get(r2.getUuid()));
        }
    }
}