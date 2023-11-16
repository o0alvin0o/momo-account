package com.momodev.service;

import com.momodev.constants.AppConfig;
import com.momodev.models.ActiveRecord;
import com.momodev.utils.AppUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class DataOperator {

    private Path rootPath;

    public DataOperator() {
    }

    public DataOperator(Path rootPath) {
        this.rootPath = rootPath;
    }

    public <T extends ActiveRecord> List<T> load(Class<T> clazz) throws IOException, ClassNotFoundException {
        Path dataPath = Path.of(getDataFilePath(clazz.getSimpleName()));
        List<T> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath)) {
            for (Path file : stream) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath + File.separator + file.getFileName()))) {
                    T record = (T) ois.readObject();
                    result.add(record);
                }
            }
        }

        return result;
    }

    public <T extends ActiveRecord> Optional<T> getById(String id, Class<T> clazz) throws IOException, ClassNotFoundException {
        List<T> records = load(clazz);
        return  records.stream()
                .filter(record -> Objects.equals(record.getId(), id))
                .findFirst();
    }

    public <T extends ActiveRecord> void persist(T record, Class<T> clazz) throws IOException {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID().toString().substring(0, 11));
        }
        String dataFilePath = getDataFilePath(clazz.getSimpleName());
        String dataFileName = Path.of(dataFilePath).resolve(record.getId()).toString();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFileName))) {
            oos.writeObject(record);
        }
    }
    public <T extends ActiveRecord> void persist(List<T> records, Class<T> clazz) throws IOException {
        for (T t : records) {
            persist(t, clazz);
        }
    }

    public <T extends ActiveRecord> void backUp(T record, Class<T> clazz) throws IOException {
        String fileName = File.separator + record.getId();
        String backUpPath = getBackupFilePath(clazz.getSimpleName()) + fileName;
        String dataPath = getDataFilePath(clazz.getSimpleName()) + fileName;
        AppUtils.moveFile(dataPath, backUpPath);
    }

    public <T extends ActiveRecord> void restore(T record, Class<T> clazz) throws IOException {
        String fileName = File.separator + record.getId();
        String backUpPath = getBackupFilePath(clazz.getSimpleName()) + fileName;
        String dataPath = getDataFilePath(clazz.getSimpleName()) + fileName;
        AppUtils.moveFile(backUpPath, dataPath);
    }

    public <T extends ActiveRecord> void deleteIfExist(T record, Class<T> clazz) throws IOException {
        String fileName = File.separator + record.getId();
        String backUpPath = getBackupFilePath(clazz.getSimpleName()) + fileName;
        String dataPath = getDataFilePath(clazz.getSimpleName()) + fileName;
        AppUtils.deleteIfExist(backUpPath);
        AppUtils.deleteIfExist(dataPath);
    }

    public <T extends ActiveRecord> void deleteBackup(T record, Class<T> clazz) throws IOException {
        String fileName = File.separator + record.getId();
        String backUpPath = getBackupFilePath(clazz.getSimpleName()) + fileName;
        AppUtils.deleteIfExist(backUpPath);
    }

    public String getDataFilePath(String className) throws IOException {
        Path mainDataFolder = rootPath.resolve(AppConfig.DATA_FOLDER_NAME);
        if (!Files.exists(mainDataFolder)) {
            Files.createDirectory(mainDataFolder);
        }
        Path dataFilePath = mainDataFolder.resolve(className.toLowerCase());
        if (!Files.exists(dataFilePath)) {
            Files.createDirectory(dataFilePath);
        }
        return dataFilePath.toString();
    }

    public String getBackupFilePath(String className) throws IOException {
        Path mainBackUpFolder = rootPath.resolve(AppConfig.BACKUP_FOLDER_NAME);
        if (!Files.exists(mainBackUpFolder)) {
            Files.createDirectory(mainBackUpFolder);
        }
        Path dataFilePath = mainBackUpFolder.resolve(className.toLowerCase());
        if (!Files.exists(dataFilePath)) {
            Files.createDirectory(dataFilePath);
        }
        return dataFilePath.toString();
    }

    public static DataOperator getDefault() throws IOException {
        Path defaultRoot = new File(".").getCanonicalFile().toPath();
        return new DataOperator(defaultRoot);
    }

    public Path getRootPath() {
        return rootPath;
    }

    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
}