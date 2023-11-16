package com.momodev.service;

import com.momodev.models.ActiveRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DataOperator {

    private Path rootPath;

    public DataOperator() {
    }

    public DataOperator(Path rootPath) {
        this.rootPath = rootPath;
    }

    public <T extends ActiveRecord> List<T> load(Class<T> clazz) {
        // TODO
        return null;
    }

    public <T extends ActiveRecord> Optional<T> getById(String id, Class<T> clazz) {
        // TODO
        return null;
    }

    public <T extends ActiveRecord> void persist(T record, Class<T> clazz) {
        // TODO
    }
    public <T extends ActiveRecord> void persist(List<T> records, Class<T> clazz) {
        // TODO
    }

    public <T extends ActiveRecord> void backUp(T record, Class<T> clazz) {
        // TODO
    }

    public <T extends ActiveRecord> void restore(T record, Class<T> clazz) {
        // TODO
    }

    public <T extends ActiveRecord> void deleteIfExist(T record, Class<T> clazz) {
        // TODO
    }

    public <T extends ActiveRecord> void deleteBackup(T record, Class<T> clazz) {
        // TODO
    }

    public String getDataFilePath(String className) {
        // TODO
        return null;
    }

    public String getBackupFilePath(String className) {
        // TODO
        return null;
    }

    public static DataOperator getDefault() {
        // TODO
        return null;
    }

    public Path getRootPath() {
        return rootPath;
    }

    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
}
