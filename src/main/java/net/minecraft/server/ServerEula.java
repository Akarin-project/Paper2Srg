package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerEula {

    private static final Logger LOG = LogManager.getLogger();
    private final File eulaFile;
    private final boolean acceptedEULA;

    public ServerEula(File file) {
        this.eulaFile = file;
        this.acceptedEULA = this.loadEULAFile(file);
    }

    private boolean loadEULAFile(File file) {
        FileInputStream fileinputstream = null;
        boolean flag = false;

        try {
            Properties properties = new Properties();

            fileinputstream = new FileInputStream(file);
            properties.load(fileinputstream);
            flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
        } catch (Exception exception) {
            ServerEula.LOG.warn("Failed to load {}", file);
            this.createEULAFile();
        } finally {
            IOUtils.closeQuietly(fileinputstream);
        }

        return flag;
    }

    public boolean hasAcceptedEULA() {
        return this.acceptedEULA;
    }

    public void createEULAFile() {
        FileOutputStream fileoutputstream = null;

        try {
            Properties properties = new Properties();

            fileoutputstream = new FileOutputStream(this.eulaFile);
            properties.setProperty("eula", "false");
            properties.store(fileoutputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula)." +
                    "\nand also agreeing that tacos are tasty.");  // Paper - fix lag
        } catch (Exception exception) {
            ServerEula.LOG.warn("Failed to save {}", this.eulaFile, exception);
        } finally {
            IOUtils.closeQuietly(fileoutputstream);
        }

    }
}
