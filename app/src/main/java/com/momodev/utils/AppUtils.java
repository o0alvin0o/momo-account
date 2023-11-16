package com.momodev.utils;

import com.momodev.constants.AppMessage;
import com.momodev.models.ActiveRecord;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AppUtils {

    private static final String DEFAULT_SPACE = "%-20s";

    private static final String SERIAL_VERSION_UID_FIELD_NAME = "serialVersionUID";

    public static void printHeaders(Class<? extends ActiveRecord> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        System.out.printf(DEFAULT_SPACE, "ID");
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase(SERIAL_VERSION_UID_FIELD_NAME)) {
                System.out.printf(DEFAULT_SPACE, field.getName().toUpperCase());
            }
        }
        System.out.println();
    }

    public static <T extends ActiveRecord> void printRecords(List<T> records, Class<T> clazz) {
        for (T record : records) {
            printRecord(record, clazz);
        }
    }

    public static  <T extends ActiveRecord> void printRecord(T record, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        System.out.printf(DEFAULT_SPACE, record.getId());
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase(SERIAL_VERSION_UID_FIELD_NAME)) {
                try {
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = clazz.getMethod(methodName);
                    Object value = method.invoke(record);
                    System.out.printf(DEFAULT_SPACE, value);
                } catch (Exception e) {
                    System.out.print(AppMessage.RESULT_PRINTING_ERR);
                }
            }
        }
        System.out.println();
    }

    public static void moveFile(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteIfExist(String source) throws IOException {
        Path sourcePath = Paths.get(source);
        Files.deleteIfExists(sourcePath);
    }
}
