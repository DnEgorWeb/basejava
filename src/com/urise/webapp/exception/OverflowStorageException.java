package com.urise.webapp.exception;

public class OverflowStorageException extends StorageException {
    public OverflowStorageException(String uuid) {
        super("No space to save resume with uuid " + uuid, uuid);
    }
}
