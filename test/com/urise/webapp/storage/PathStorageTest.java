package com.urise.webapp.storage;

import com.urise.webapp.storage.serialization.ObjectStreamSerializer;

public class PathStorageTest extends AbstractStorageTest {
    protected PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}
