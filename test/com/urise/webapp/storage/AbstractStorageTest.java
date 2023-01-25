package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

abstract class AbstractStorageTest {
    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
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
            assertGet(r);
        }
    }

    @Nested
    @DisplayName("save")
    class Save {
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
            assertSize(1);
            assertGet(r);
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
            Resume r1 = new Resume(r.getUuid(), "");
            storage.update(r1);
            assertSize(1);
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
            assertSize(0);
            assertThrows(NotExistStorageException.class, () -> storage.get(r.getUuid()));
        }
    }

    @Nested
    @DisplayName("size")
    class Size {
        @Test
        @DisplayName("Returns 0 when empty")
        void returnsZeroWhenEmpty() {
            assertSize(0);
        }

        @Test
        @DisplayName("Returns value equal to amount of saved resumes")
        void returnsValueEqualToAmountOfSavedResumes() {
            storage.save(new Resume());
            storage.save(new Resume());
            storage.save(new Resume());
            assertSize(3);
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
            assertSize(0);
        }
    }

    @Nested
    @DisplayName("getAll")
    class GetAllSorted {
        @Test
        @DisplayName("Returns empty array when no resumes in storage")
        void returnsEmptyArrayWhenNoResumes() {
            assertEquals(0, storage.getAllSorted().size());
        }

        @Test
        @DisplayName("Returns all resumes sorted")
        void returnsAllResumesSorted() {
            String uuid1 = UUID.randomUUID().toString();
            String uuid2 = UUID.randomUUID().toString();
            String uuid3 = UUID.randomUUID().toString();
            String uuid4 = UUID.randomUUID().toString();
            String uuid5 = UUID.randomUUID().toString();
            Resume r1 = new Resume(uuid1, "Alan");
            Resume r2 = new Resume(uuid2, "Brad");
            Resume r3 = new Resume(uuid3, "Calvin");
            Resume r4 = new Resume(uuid4, "Calvin");
            Resume r5 = new Resume(uuid5, "Den");
            storage.save(r3);
            storage.save(r5);
            storage.save(r2);
            storage.save(r1);
            storage.save(r4);
            List<Resume> resumes = storage.getAllSorted();
            assertEquals(5, resumes.size());
            assertEquals(r1, resumes.get(0));
            assertEquals(r2, resumes.get(1));
            assertEquals(r5, resumes.get(4));
        }
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}
