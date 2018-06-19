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

    private static final Logger field_164440_a = LogManager.getLogger();
    public final Properties field_73672_b = new Properties();
    private final File field_73673_c;

    public PropertyManager(File file) {
        this.field_73673_c = file;
        if (file.exists()) {
            FileInputStream fileinputstream = null;

            try {
                fileinputstream = new FileInputStream(file);
                this.field_73672_b.load(fileinputstream);
            } catch (Exception exception) {
                PropertyManager.field_164440_a.warn("Failed to load {}", file, exception);
                this.func_73666_a();
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
            PropertyManager.field_164440_a.warn("{} does not exist", file);
            this.func_73666_a();
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

    public void func_73666_a() {
        PropertyManager.field_164440_a.info("Generating new properties file");
        this.func_73668_b();
    }

    public void func_73668_b() {
        FileOutputStream fileoutputstream = null;

        try {
            // CraftBukkit start - Don't attempt writing to file if it's read only
            if (this.field_73673_c.exists() && !this.field_73673_c.canWrite()) {
                return;
            }
            // CraftBukkit end

            fileoutputstream = new FileOutputStream(this.field_73673_c);
            this.field_73672_b.store(fileoutputstream, "Minecraft server properties");
        } catch (Exception exception) {
            PropertyManager.field_164440_a.warn("Failed to save {}", this.field_73673_c, exception);
            this.func_73666_a();
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

    public File func_73665_c() {
        return this.field_73673_c;
    }

    public String func_73671_a(String s, String s1) {
        if (!this.field_73672_b.containsKey(s)) {
            this.field_73672_b.setProperty(s, s1);
            this.func_73668_b();
            this.func_73668_b();
        }

        return getOverride(s, this.field_73672_b.getProperty(s, s1)); // CraftBukkit
    }

    public int func_73669_a(String s, int i) {
        try {
            return getOverride(s, Integer.parseInt(this.func_73671_a(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            this.field_73672_b.setProperty(s, "" + i);
            this.func_73668_b();
            return getOverride(s, i); // CraftBukkit
        }
    }

    public long func_179885_a(String s, long i) {
        try {
            return getOverride(s, Long.parseLong(this.func_73671_a(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            this.field_73672_b.setProperty(s, "" + i);
            this.func_73668_b();
            return getOverride(s, i); // CraftBukkit
        }
    }

    public boolean func_73670_a(String s, boolean flag) {
        try {
            return getOverride(s, Boolean.parseBoolean(this.func_73671_a(s, "" + flag))); //CraftBukkit
        } catch (Exception exception) {
            this.field_73672_b.setProperty(s, "" + flag);
            this.func_73668_b();
            return getOverride(s, flag); // CraftBukkit
        }
    }

    public void func_73667_a(String s, Object object) {
        this.field_73672_b.setProperty(s, "" + object);
    }

    public boolean func_187239_a(String s) {
        return this.field_73672_b.containsKey(s);
    }

    public void func_187238_b(String s) {
        this.field_73672_b.remove(s);
    }
}
