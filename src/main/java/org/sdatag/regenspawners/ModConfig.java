package org.sdatag.regenspawners;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ModConfig {
    private static final String CONFIG_FILE = "config/regenspawners.properties";
    private static Properties properties = new Properties();

    public static int LIFESPAN;
    public static int LIGHT_CLEAR_RADIUS;

    public static void loadConfig() {
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
            LIFESPAN = Integer.parseInt(properties.getProperty("spawner.lifespan", "24000"));
            LIGHT_CLEAR_RADIUS = Integer.parseInt(properties.getProperty("light.clear.radius", "7"));
        } catch (IOException e) {
            System.err.println("Failed to load config, using default values");
            LIFESPAN = 24000;
            LIGHT_CLEAR_RADIUS = 7;
        }
    }

    public static void saveDefaultConfig() {
        properties.setProperty("spawner.lifespan", String.valueOf(LIFESPAN));
        properties.setProperty("light.clear.radius", String.valueOf(LIGHT_CLEAR_RADIUS));
        try {
            properties.store(new FileOutputStream(CONFIG_FILE), "RegenSpawners Mod Configuration");
        } catch (IOException e) {
            System.err.println("Failed to save default config");
        }
    }
}
