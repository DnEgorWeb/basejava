package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serialization.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerializer serializer;

    protected PathStorage(String dir, StreamSerializer serializer) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(serializer, "serializer must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
        this.serializer = serializer;
    }

    @Override
    protected List<Resume> doGetAll() {
        return getPathStream().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    protected Boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(Paths.get(uuid));
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.toString(), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        doUpdate(r, path);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            serializer.doWrite(r, new BufferedOutputStream(Files.newOutputStream(directory.resolve(path))));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.toString(), e);
        }
    }

    @Override
    public void clear() {
        getPathStream().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return Math.toIntExact(getPathStream().count());
    }

    private Stream<Path> getPathStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Get path stream error", null, e);
        }
    }
}
