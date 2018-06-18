package net.minecraft.server.dedicated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import net.minecraft.server.OptionSet;

public class PropertyManager {

    private static final Logger LOGGER = LogManager.getLogger();
    public final Properties serverProperties = new Properties();
    private final File serverPropertiesFile;

    public PropertyManager(File file) {
        this.serverPropertiesFile = file;
        if (file.exists()) {
            FileInputStream fileinputstream = null;

            try {
                fileinputstream = new FileInputStream(file);
                this.serverProperties.load(fileinputstream);
            } catch (Exception exception) {
                PropertyManager.LOGGER.warn("Failed to load {}", file, exception);
                this.generateNewProperties();
            } finally {
                if (fileinputstream != null) {
                    try {
                        fileinputstream.close();
                    } catch (IOException ioexception) {
                        ;
                    }
                }

            }
        } else {
            PropertyManager.LOGGER.warn("{} does not exist", file);
            this.generateNewProperties();
        }

    }

    // CraftBukkit start
    private OptionSet options = null;

    public PropertyManager(final OptionSet options) {
        this((File) options.valueOf("config"));

        this.options = options;
    }

    private <T> T getOverride(String name, T value) {
        if ((this.options != null) && (this.options.has(name)) && !name.equals( "online-mode")) { // Spigot
            return (T) this.options.valueOf(name);
        }

        return value;
    }
    // CraftBukkit end

    public void generateNewProperties() {
        PropertyManager.LOGGER.info("Generating new properties file");
        this.saveProperties();
    }

    public void saveProperties() {
        FileOutputStream fileoutputstream = null;

        try {
            // CraftBukkit start - Don't attempt writing to file if it's read only
            if (this.serverPropertiesFile.exists() && !this.serverPropertiesFile.canWrite()) {
                return;
            }
            // CraftBukkit end

            fileoutputstream = new FileOutputStream(this.serverPropertiesFile);
            this.serverProperties.store(fileoutputstream, "Minecraft server properties");
        } catch (Exception exception) {
            PropertyManager.LOGGER.warn("Failed to save {}", this.serverPropertiesFile, exception);
            this.generateNewProperties();
        } finally {
            if (fileoutputstream != null) {
                try {
                    fileoutputstream.close();
                } catch (IOException ioexception) {
                    ;
                }
            }

        }

    }

    public File getPropertiesFile() {
        return this.serverPropertiesFile;
    }

    public String getStringProperty(String s, String s1) {
        if (!this.serverProperties.containsKey(s)) {
            this.serverProperties.setProperty(s, s1);
            this.saveProperties();
            this.saveProperties();
        }

        return getOverride(s, this.serverProperties.getProperty(s, s1)); // CraftBukkit
    }

    public int getIntProperty(String s, int i) {
        try {
            return getOverride(s, Integer.parseInt(this.getStringProperty(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            this.serverProperties.setProperty(s, "" + i);
            this.saveProperties();
            return getOverride(s, i); // CraftBukkit
        }
    }

    public long getLongProperty(String s, long i) {
        try {
            return getOverride(s, Long.parseLong(this.getStringProperty(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            this.serverProperties.setProperty(s, "" + i);
            this.saveProperties();
            return getOverride(s, i); // CraftBukkit
        }
    }

    public boolean getBooleanProperty(String s, boolean flag) {
        try {
            return getOverride(s, Boolean.parseBoolean(this.getStringProperty(s, "" + flag))); //CraftBukkit
        } catch (Exception exception) {
            this.serverProperties.setProperty(s, "" + flag);
            this.saveProperties();
            return getOverride(s, flag); // CraftBukkit
        }
    }

    public void setProperty(String s, Object object) {
        this.serverProperties.setProperty(s, "" + object);
    }

    public boolean hasProperty(String s) {
        return this.serverProperties.containsKey(s);
    }

    public void removeProperty(String s) {
        this.serverProperties.remove(s);
    }
}
