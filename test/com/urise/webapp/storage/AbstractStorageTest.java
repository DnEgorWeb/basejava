package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    @AfterEach
    void cleanUp() {
        storage.clear();
    }

    @Nested
    @DisplayName("get")
    class Get {
        @Test
        @DisplayName("Throws not exists when no matching resume")
        void throwsNotExistsWhenNoMatchingResume() {
            assertThrows(NotExistStorageException.class, () -> storage.get(UUID.randomUUID()
                    .toString()));
        }

        @Test
        @DisplayName("Returns resume")
        void returnsResume() {
            Resume r = ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock");
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
            Resume r = ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock");
            storage.save(r);
            assertThrows(ExistStorageException.class, () -> storage.save(r));
        }

        @Test
        @DisplayName("Saves resume to the storage")
        void savesResumeToStorage() {
            Resume r = ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock");
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
            assertThrows(NotExistStorageException.class, () -> storage.update(ResumeTestData.buildResume(UUID.randomUUID()
                    .toString(), "mock")));
        }

        @Test
        @DisplayName("Updates resume")
        void updatesResume() {
            Resume r = ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock");
            storage.save(r);
            assumeTrue(storage.size() == 1);
            assumeTrue(r.equals(storage.get(r.getUuid())));
            Resume r1 = ResumeTestData.buildResume(r.getUuid(), "mock2");
            Map<ContactType, String> contacts = new HashMap<>();
            contacts.put(ContactType.PHONE, "mock phone");
            contacts.put(ContactType.SKYPE, "mock skype");
            contacts.put(ContactType.EMAIL, "mock email");
            contacts.put(ContactType.LINKEDIN, "mock linkedin");
            contacts.put(ContactType.GITHUB, "mock github");
            contacts.put(ContactType.STACKOVERFLOW, "mock stackoverflow");
            contacts.put(ContactType.HOMEPAGE, "mock website");
            r1.setContacts(contacts);
            storage.update(r1);
            assertSize(1);
            Resume storageResume = storage.get(r.getUuid());
            assertEquals(r1, storageResume);
            assertNotSame(r, storageResume);
            assertEquals(contacts, storageResume.getContacts());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {
        @Test
        @DisplayName("Throws not exists when no matching resume")
        void throwsNotExistsWhenNoMatchingResume() {
            assertThrows(NotExistStorageException.class, () -> storage.delete(UUID.randomUUID()
                    .toString()));
        }

        @Test
        @DisplayName("Deletes resume")
        void deletesResume() {
            Resume r = ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock");
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
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
            assertSize(3);
        }
    }

    @Nested
    @DisplayName("clear")
    class Clear {
        @Test
        @DisplayName("Clears storage")
        void clearsStorage() {
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
            storage.save(ResumeTestData.buildResume(UUID.randomUUID().toString(), "mock"));
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
            Resume r1 = ResumeTestData.buildResume(UUID.randomUUID().toString(), "Alan");
            Resume r2 = ResumeTestData.buildResume(UUID.randomUUID().toString(), "Brad");
            Resume r3 = ResumeTestData.buildResume(UUID.randomUUID().toString(), "Calvin");
            Resume r4 = ResumeTestData.buildResume(UUID.randomUUID().toString(), "Calvin");
            Resume r5 = ResumeTestData.buildResume(UUID.randomUUID().toString(), "Den");
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
}
