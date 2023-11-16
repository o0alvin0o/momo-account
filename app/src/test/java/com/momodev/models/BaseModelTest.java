package com.momodev.models;

import com.momodev.constants.AppConfig;
import com.momodev.service.DataOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseModelTest {

    protected DataOperator dataOperator = new DataOperator(root);

    @TempDir
    protected static Path root;

    @BeforeAll
    static void setUp() throws IOException {
        Files.createDirectory(root.resolve(AppConfig.DATA_FOLDER_NAME));
    }
}
