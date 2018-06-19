package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerEula {

    private static final Logger field_154349_a = LogManager.getLogger();
    private final File field_154350_b;
    private final boolean field_154351_c;

    public ServerEula(File file) {
        this.field_154350_b = file;
        this.field_154351_c = this.func_154347_a(file);
    }

    private boolean func_154347_a(File file) {
        FileInputStream fileinputstream = null;
        boolean flag = false;

        try {
            Properties properties = new Properties();

            fileinputstream = new FileInputStream(file);
            properties.load(fileinputstream);
            flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
        } catch (Exception exception) {
            ServerEula.field_154349_a.warn("Failed to load {}", file);
            this.func_154348_b();
        } finally {
            IOUtils.closeQuietly(fileinputstream);
        }

        return flag;
    }

    public boolean func_154346_a() {
        return this.field_154351_c;
    }

    public void func_154348_b() {
        FileOutputStream fileoutputstream = null;

        try {
            Properties properties = new Properties();

            fileoutputstream = new FileOutputStream(this.field_154350_b);
            properties.setProperty("eula", "false");
            properties.store(fileoutputstream, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula)." +
                    "\nand also agreeing that tacos are tasty.");  // Paper - fix lag
        } catch (Exception exception) {
            ServerEula.field_154349_a.warn("Failed to save {}", this.field_154350_b, exception);
        } finally {
            IOUtils.closeQuietly(fileoutputstream);
        }

    }
}
