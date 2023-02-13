package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serialization.StreamSerializer;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamSerializer serializer;

    protected FileStorage(File directory, StreamSerializer serializer) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(serializer, "serializer must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.serializer = serializer;
    }

    @Override
    public void clear() {
        for (File file : getCheckedListFiles()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getCheckedListFiles().size();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            serializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File was not deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<File> fileList = getCheckedListFiles();
        return fileList.stream().map(this::doGet).collect(Collectors.toList());
    }

    private List<File> getCheckedListFiles() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("failed to get files", null);
        }
        return Arrays.asList(files);
    }
}
