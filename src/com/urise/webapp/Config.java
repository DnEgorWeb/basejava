package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File("config/resume.properties");
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final String dbUser;
    private final String dbUrl;
    private final String dbPassword;
    private final String launchSqlScript;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUser = props.getProperty("db.user");
            dbUrl = props.getProperty("db.url");
            dbPassword = props.getProperty("db.password");
            String scriptPath = props.getProperty("sql.load.script"); // optional prop
            if (scriptPath != null) {
                launchSqlScript = String.join("", Files.readAllLines(Paths.get(scriptPath), Charset.defaultCharset()));
            } else {
                launchSqlScript = null;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getDbUrl() { return dbUrl; }

    public String getDbUsername() { return dbUser; }

    public String getDbPassword() { return dbPassword; }

    public String getLaunchSqlScript() { return launchSqlScript; }
}
